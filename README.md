# Bottify

A private, single-user music streaming server for the CDs you own — the ones that never
made it to Spotify or Apple Music. Rip your discs, upload them to your own VPS, and stream
them to your browser.

Built from scratch as a learning project: a custom HTTP API and web player on a Java 25 /
Spring Boot backend, with lossless FLAC storage and on-the-fly transcoding.

## Stack

- **Backend:** Java 25 (current LTS), Spring Boot 4.0.x
- **Database:** MariaDB 11 LTS (Flyway migrations) — metadata only; audio stays on disk
- **Secrets:** MariaDB credentials fetched from a remote HashiCorp Vault (Spring Cloud Vault)
- **Search:** deferred to a dedicated Elasticsearch service in a later phase
- **Audio:** FLAC stored lossless, transcoded on the fly via ffmpeg
- **Deployment:** Docker Compose behind Caddy (automatic HTTPS)

## Documentation

- [Architecture overview](docs/architecture.md) — the map: components, topology, data model, roadmap
- [Architecture Decision Records](adr/README.md) — the *why* behind the significant choices

## Modules

Bottify is a modular monolith laid out as a hexagon (Ports & Adapters). Dependencies point
**inward**: adapters depend on the application, the application depends on the domain, and
the domain depends on nothing. Only `bottify-bootstrap` knows every adapter and assembles
the single deployable. The four adapters are independent of each other, so `mvnd` builds
them in parallel.

| Module | What it is |
|--------|------------|
| `bottify-domain` | **The core.** Entities, value objects and business rules (artists, albums, tracks, playlists, users). Pure Java — no Spring, no JPA. Touch this when a *rule* changes ("a track belongs to exactly one album"), never for wiring or I/O. Everything depends on it; it depends on nothing. |
| `bottify-application` | **The use cases and the ports.** Defines what the app can do (`port/in`, e.g. *stream a track*, *scan the library*) and what it needs from the outside (`port/out`, e.g. `TrackRepository`, `AudioStorage`, `Transcoder`), and orchestrates the domain to fulfill them. Still framework-free. Touch this to add or change a feature's behavior. |
| `bottify-adapter-rest` | **Inbound (driving) adapter.** The custom HTTP/JSON API the web player calls, plus authentication (Spring Security). Translates HTTP requests into inbound-port calls. Touch this to add an endpoint or change request/response shapes or auth. (The web player SPA is a separate frontend served by Caddy, not built here.) |
| `bottify-adapter-persistence` | **Outbound (driven) adapter for MariaDB.** Implements the repository ports with Spring Data JPA and owns the JPA entities and the Flyway migrations (`db/migration/`). The DB holds metadata only. Touch this to change how data is stored or to add a schema migration. |
| `bottify-adapter-filestore` | **Outbound adapter for audio on disk.** Stores/locates FLAC files, reads bytes with range support for streaming, and reads tags + cover art via JAudiotagger. Touch this to change how files are read from or written to the media directory. |
| `bottify-adapter-transcoder` | **Outbound adapter for transcoding.** Implements the transcoding port by invoking `ffmpeg` and streaming its output. Touch this to change transcode formats, bitrates, or caching. |
| `bottify-bootstrap` | **The composition root and only deployable.** Depends on all adapters, component-scans them, wires the framework-free application services into Spring beans, and owns cross-cutting concerns (Actuator/metrics), secret retrieval from HashiCorp Vault (Spring Cloud Vault), and `application.yml`. This is the module you run and containerize. Touch it for app-wide config and bean wiring. |

> **Rule of thumb:** business logic goes *inward* (domain/application); anything that talks
> to the outside world — HTTP, the database, the filesystem, ffmpeg — goes in an *adapter*.
> If the core ever needs to import a framework class, the logic is in the wrong place.

## Status

Early days — the architecture is designed and recorded; the Maven module skeletons build.
Application code comes next. See the [build roadmap](docs/architecture.md#build-roadmap)
for the plan.
