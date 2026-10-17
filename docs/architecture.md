# Bottify — Architecture Overview

> A private, single-user CD streaming server. You rip CDs you own, upload them to your
> own VPS, and stream them to your browser. Built from scratch, primarily as a learning
> project.

This document is the high-level map. The *reasoning* behind the significant choices lives
in the [Architecture Decision Records](../adr/README.md) — this overview links to them
rather than repeating the justifications.

## Locked decisions

| Aspect | Choice | Rationale |
|--------|--------|-----------|
| Users | Single user (you) | — |
| Hosting | One Linux VPS | [ADR-0002](../adr/0002-single-vps-modular-monolith.md) |
| Shape | Modular monolith (no k8s / microservices) | [ADR-0002](../adr/0002-single-vps-modular-monolith.md) |
| API & clients | **Custom API + custom web player** (not Subsonic) | [ADR-0003](../adr/0003-build-custom-api-not-subsonic.md) |
| Backend | Java 25 / Spring Boot 4.0.x | [ADR-0004](../adr/0004-java-spring-boot-backend.md) |
| Metadata store | MariaDB 11 LTS (Flyway migrations) | [ADR-0005](../adr/0005-mariadb-metadata-files-on-disk.md) |
| Audio store | FLAC files on the filesystem | [ADR-0005](../adr/0005-mariadb-metadata-files-on-disk.md) |
| Search | Deferred to a dedicated Elasticsearch service (later phase) | — |
| Delivery | Store lossless, transcode on the fly (ffmpeg) | [ADR-0006](../adr/0006-lossless-storage-on-the-fly-transcode.md) |
| DB secrets | Fetched from remote HashiCorp Vault via Spring Cloud Vault | [ADR-0007](../adr/0007-mariadb-credentials-from-vault.md) |

> **Note:** Because Bottify uses a custom API rather than Subsonic, the web browser is the
> **only** client. Third-party player apps won't work unless a Subsonic-compatible adapter
> is added later — see [ADR-0003](../adr/0003-build-custom-api-not-subsonic.md).

## System context

```mermaid
flowchart LR
    You([You]) --> Browser[Web player<br/>your SPA]
    Browser -->|custom HTTP API| API
    subgraph VPS[Your VPS]
      API[Bottify<br/>Spring Boot]
      API --> DB[(MariaDB<br/>metadata)]
      API --> FS[[FLAC files<br/>on disk]]
      API --> FF[ffmpeg<br/>transcode]
    end
    Rip[Rip CDs on<br/>your laptop] -.->|rsync over SSH| FS
```

*Dashed = one-time ingest path · solid = live streaming path.*

## Internal modules

A single deployable app, cleanly separated inside:

- **Library** — watches the media directory, reads tags via JAudiotagger, extracts cover
  art, builds the artist → album → track catalog. Idempotent rescans.
- **Streaming** — serves audio with HTTP range requests (seeking); chooses direct-play
  vs. transcode.
- **Transcoder** — invokes `ffmpeg` via `ProcessBuilder`, streaming output to the
  response; on-disk cache keyed by `(track, format, bitrate)`.
- **API** — the custom HTTP endpoints the web player consumes.
- **Auth & sessions** — Spring Security; session/JWT login, bcrypt-hashed credentials.
- **Web player** — the SPA client (browse, queue, play, seek, search, playlists).

## Deployment topology

```mermaid
flowchart TB
    Net([Internet]) -->|443 HTTPS| Caddy
subgraph host[Rocky Linux VM]
Caddy[Caddy<br/>reverse proxy + auto-TLS]
Caddy -->|:8080|App[bottify<br/>Spring Boot non-root]
subgraph compose[VPS Docker Compose]
App -. reads .-> V1[[vol: media / FLAC]]
Backup[restic · nightly] -.-> V1
end
Backup -.-> PG
App --> PG[(mariadb:11)]
Backup -->|encrypted|Off[(Off-site storage)]
end
```

| Service | Image | Role | Exposed |
|---------|-------|------|---------|
| caddy | `caddy:2` | TLS termination, reverse proxy, security headers | 80, 443 |
| bottify | your build (GHCR) | The app; ffmpeg baked in | internal only |
| mariadb | `mariadb:11` | Metadata, users, playlists, history | internal only |
| backup | `restic` + cron | Encrypted nightly snapshots off-site | none |

See [ADR-0007](../adr/0007-docker-compose-caddy-deployment.md).

## Data model

The domain model lives in `bottify-domain` (`de.bottify.domain`) as plain, framework-free
Java records. It's made of two aggregates, **Artist** and **Album**, plus immutable value
objects.

```mermaid
classDiagram
  direction LR

  class Artist {
<<AggregateRoot>>
+ArtistId artistId
+String name
+Rating rating
}

class Album {
<<AggregateRoot>>
+AlbumId albumId
+ArtistId artistId
+String title
+LocalDate releaseDate
+Genre genre
+List~Track~ tracks
+Rating rating
+trackCount() int
+totalDuration() Duration
}

class Track {
<<Entity>>
+TrackId trackId
+String title
+Duration duration
+Rating rating
+int trackNumber
+int discNumber
+Comparator~Track~ BY_POSITION$
}

class Genre {
<<ValueObject>>
+String name
+Genre HOERSPIEL$
+key() String
 }

class Rating {
<<ValueObject>>
+int value
+int MIN$ = 0
+int MAX$ = 5
+compareTo(Rating) int
 }

class ArtistId {
<<ValueObject>>
+UUID artistId
+fromString(String)$ ArtistId
}

class AlbumId {
<<ValueObject>>
+UUID albumId
+fromString(String)$ AlbumId
}

class TrackId {
<<ValueObject>>
+UUID trackId
+fromString(String)$ TrackId
}

Album "1" *-- "0..*" Track: tracks, ordered BY_POSITION
Album "0..*" ..> "1" Artist: references by artistId

Artist *-- "1" ArtistId : identity
Album *-- "1" AlbumId : identity
Track *-- "1" TrackId : identity

Album --> "1" Genre
Artist --> "1" Rating
Album --> "1" Rating
Track --> "1" Rating
```

How to read it:

- **Album is the consistency boundary for its tracks.** `Track` has no back-reference to
  its album, so you can only get to a track through its `Album` (composition, ◆). The album
  keeps `tracks` immutable and always sorted by `Track.BY_POSITION` (disc → track number →
  id as a tiebreaker), and computes `trackCount()` and `totalDuration()` from them.
- **Artist and Album are separate aggregates.** An album points to its artist **by
  identity** (`ArtistId`), not by object reference (dashed arrow). Neither side holds a
  collection of the other, so each can be loaded and saved on its own.
- **Entities compare by identity.** `Artist`, `Album` and `Track` override
  `equals`/`hashCode` to compare only their typed id (`ArtistId`, `AlbumId`, `TrackId`, each
  a UUID wrapper).
- **Value objects compare by value and check their own inputs.** `Rating` is limited to
  0–5 (0 = unrated). `Genre` compares case-insensitively via `key()` but keeps the original
  spelling for display.

Technical file metadata (file path, codec, bitrate, size, cover-art path) is not part of
the domain model yet. Metadata lives in MariaDB and audio stays on disk —
[ADR-0005](../adr/0005-mariadb-metadata-files-on-disk.md).

### Planned (not implemented yet)

Users, playlists and listening history aren't in `bottify-domain` yet. This is a sketch
of how they are expected to attach to the catalog. Like `Album → Artist`, they reference
tracks by `TrackId` rather than holding `Track` objects:

```mermaid
classDiagram
  direction LR

  class AppUser {
    <<planned>>
  }
  class Playlist {
    <<planned>>
    +String name
    +List~TrackId~ entries
  }
  class PlayEvent {
    <<planned>>
    +TrackId trackId
    +Instant playedAt
  }
  class TrackId {
    <<Value Object>>
  }

  AppUser "1" --> "0..*" Playlist : owns
  AppUser "1" --> "0..*" PlayEvent : logs
  Playlist ..> "0..*" TrackId : ordered entries
  PlayEvent ..> "1" TrackId : played track
```

## Getting CDs in

The fastest path to listening is to rip and tag on your laptop, then sync files to the
server — no in-app upload needed at first.

1. **Rip** each CD to FLAC with accurate-rip verification (fre:ac / EAC / whipper).
2. **Tag** from MusicBrainz (Picard) so artist/album/track/art are clean.
3. **Sync**: `rsync -av ./music/ user@vps:/srv/bottify/media/`.
4. **Scan**: trigger a library rescan; Bottify reads tags, dedups, updates the catalog.

An in-app drag-and-drop upload page is a natural later feature — it drops files into the
same media folder and fires a scan.

## Search (planned, deferred)

Search is intentionally **not** a job for the relational database. Early on, simple
`LIKE`-style lookups over the small catalog are enough. When richer search is wanted —
fuzzy, typo-tolerant, accent-aware ranking — it will be added as a **dedicated
Elasticsearch service**, indexed from the catalog, rather than by leaning on MariaDB's
full-text features. This is a deliberate choice (also a learning goal) and is the reason
the database engine was chosen on operational grounds, not text-search capability —
see [ADR-0005](../adr/0005-mariadb-metadata-files-on-disk.md).

## Production readiness

- **Observability** — Spring Boot Actuator health/readiness probes, structured JSON logs,
  Micrometer → Prometheus metrics (optional Grafana).
- **Backups** — nightly `restic` (media + `mariadb-dump`), encrypted, off-site; **test restores**.
- **CI/CD** — GitHub Actions builds/tests/scans the image, pushes to GHCR on tag, deploys
  via SSH + `docker compose pull && up -d`.
- **Host hardening** — SSH keys only, firewall (22/80/443), fail2ban, unattended security
  updates.
- **Config** — 12-factor; all config via env, nothing environment-specific in the image.
- **Secrets** — MariaDB credentials are pulled at startup from a remote HashiCorp Vault
  (Spring Cloud Vault, wired in `bottify-bootstrap`), never committed or baked in; the
  persistence adapter is unaware — [ADR-0008](../adr/0007-mariadb-credentials-from-vault.md).
- **Reliability** — `restart: unless-stopped`, health-check-driven restarts, resource
  limits so a runaway transcode can't OOM the box.

## Risks & things to decide early

- **Transcoding is CPU-bound.** Fine for one listener on ~2 vCPU, but cache aggressively
  and prefer direct-play — [ADR-0006](../adr/0006-lossless-storage-on-the-fly-transcode.md).
- **Filesystem ↔ catalog consistency.** The scanner is the single reconciler for files
  added/moved/removed out of band — [ADR-0005](../adr/0005-mariadb-metadata-files-on-disk.md).
- **Keep it single-user and private.** Ripping CDs you own for your own private listening
  is a personal-copy scenario; keeping Bottify locked to one authenticated account is the
  clean design. (Not legal advice.)

## Build roadmap

Each phase is usable and de-risks the next. You're streaming your own CDs by Phase 2.

| Phase | Focus | Goal |
|-------|-------|------|
| **0 — Foundation** | VPS + domain + Compose + Caddy TLS + empty Spring Boot app + MariaDB + Flyway | A "hello" build live over HTTPS |
| **1 — Library** | Media folder + JAudiotagger scan → catalog; browse endpoints + cover art | Your catalog served by the API |
| **2 — Playback** | `stream` with range requests (direct-play), then ffmpeg transcode + cache; auth | **Actually listening 🎧** |
| **3 — Web player** | SPA: browse, play, queue, seek; login; search + playlists | Your own front door |
| **4 — Hardening** | Backups + restore test, monitoring, CI/CD, host hardening, resource limits | Set-and-forget reliability |

---

*Prepared 2026-09-13. Decisions are recorded in [`adr/`](../adr/README.md); update this
overview whenever a new ADR is accepted.*
