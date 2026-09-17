# Architecture Decision Records

This directory records the significant architectural decisions for **Bottify**, a
private single-user CD streaming server.

We use lightweight ADRs in the [Michael Nygard format](https://cognitect.com/blog/2011/11/15/documenting-architecture-decisions).
Each record captures one decision: the **context** that forced it, the **decision**
itself, and the **consequences** we accept as a result.

## Conventions

- Files are named `NNNN-title-in-kebab-case.md`, numbered sequentially.
- A record is immutable once **Accepted**. To change a decision, write a *new* ADR
  that supersedes the old one, and update the old one's status to `Superseded by ADR-NNNN`.
- Status is one of: `Proposed`, `Accepted`, `Deprecated`, `Superseded by ADR-NNNN`.

## Index

| ADR | Title | Status |
|-----|-------|--------|
| [0001](0001-record-architecture-decisions.md) | Record architecture decisions | Accepted |
| [0002](0002-single-vps-modular-monolith.md) | Single-VPS modular monolith, not microservices | Accepted |
| [0003](0003-build-custom-api-not-subsonic.md) | Build a custom API and web player instead of adopting Subsonic | Accepted |
| [0004](0004-java-spring-boot-backend.md) | Java 25 / Spring Boot for the backend | Accepted |
| [0005](0005-mariadb-metadata-files-on-disk.md) | Metadata in MariaDB, audio on the filesystem | Accepted |
| [0006](0006-lossless-storage-on-the-fly-transcode.md) | Store lossless, transcode on the fly | Accepted |
| [0007](0007-docker-compose-caddy-deployment.md) | Deploy with Docker Compose behind Caddy | Accepted |
| [0008](0008-mariadb-credentials-from-vault.md) | MariaDB credentials retrieved from HashiCorp Vault (remote) | Accepted |
