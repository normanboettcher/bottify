# ADR-0007: Deploy with Docker Compose behind Caddy

- **Status:** Accepted
- **Date:** 2026-09-13

## Context

Bottify runs on a single VPS ([ADR-0002](0002-single-vps-modular-monolith.md)) and must
be reachable over HTTPS on the public internet. We want reproducible, one-command deploys
and automatic TLS, without the overhead of a full orchestrator.

## Decision

Define the whole runtime as containers in a single `docker-compose.yml`:

- **caddy** — reverse proxy terminating TLS with automatic Let's Encrypt certificates,
  HTTP/2, and security headers.
- **bottify** — the application image (multi-stage build: JRE + ffmpeg, runs as a
  non-root user), not exposed directly to the internet.
- **mariadb** — `mariadb:11` (LTS), internal only.
- **backup** — a scheduled `restic` job taking encrypted nightly snapshots (media +
  `mariadb-dump`) to off-site storage.

State lives in two named volumes: the media directory and the MariaDB data directory.
Configuration is injected via environment/Docker secrets (12-factor); nothing
environment-specific is baked into the image.

## Consequences

- Automatic HTTPS with effectively zero certificate management (Caddy's headline feature).
- One-command, reproducible deploys: `docker compose pull && docker compose up -d`,
  driven from CI.
- The app is never exposed directly; only Caddy publishes ports 80/443.
- Compose provides restart policies and health checks but no self-healing across nodes —
  acceptable for a single-node, single-user system.
- Off-site, encrypted backups are part of the topology from the start, not an
  afterthought. A backup is only real once a restore has been tested.
