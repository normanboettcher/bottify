# Bottify

A private, single-user music streaming server for the CDs you own — the ones that never
made it to Spotify or Apple Music. Rip your discs, upload them to your own VPS, and stream
them to your browser.

Built from scratch as a learning project: a custom HTTP API and web player on a Java 25 /
Spring Boot backend, with lossless FLAC storage and on-the-fly transcoding.

## Stack

- **Backend:** Java 25 (current LTS), Spring Boot 4.0.x
- **Database:** MariaDB 11 LTS (Flyway migrations) — metadata only; audio stays on disk
- **Search:** deferred to a dedicated Elasticsearch service in a later phase
- **Audio:** FLAC stored lossless, transcoded on the fly via ffmpeg
- **Deployment:** Docker Compose behind Caddy (automatic HTTPS)

## Documentation

- [Architecture overview](docs/architecture.md) — the map: components, topology, data model, roadmap
- [Architecture Decision Records](adr/README.md) — the *why* behind the significant choices

## Status

Early days — the architecture is designed and recorded; application code comes next.
See the [build roadmap](docs/architecture.md#build-roadmap) for the plan.
