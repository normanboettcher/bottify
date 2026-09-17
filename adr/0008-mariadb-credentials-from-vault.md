# ADR-0008: MariaDB credentials retrieved from HashiCorp Vault (remote)

- **Status:** Accepted
- **Date:** 2026-09-17

## Context

The MariaDB credentials ([ADR-0005](0005-mariadb-metadata-files-on-disk.md)) must not be
committed to the repository, baked into the container image, or left in plaintext env
files on the VPS. A **HashiCorp Vault** instance already exists, running on a **remote
cloud instance** — it is *not* run locally and Bottify does not host it. The application
must fetch its database credentials from that remote Vault at startup, over TLS.

This is an infrastructure/config concern, not a domain concern. In hexagonal terms the
application core has no "get a secret" use case: only the framework's datasource needs
credentials. So the integration must sit at the composition root and stay invisible to
the persistence adapter, which should continue to read standard `spring.datasource.*`
properties without knowing their origin.

## Decision

Use **Spring Cloud Vault** (release train 2025.1.x, Vault module 5.0.x — the line
compatible with Spring Boot 4.0 / Spring Framework 7) to source MariaDB credentials from
the remote Vault, wired via the config-data import `spring.config.import: vault://`.

- The dependency (`spring-cloud-starter-vault-config`) lives **only in
  `bottify-bootstrap`**, the composition root. `bottify-adapter-persistence` is
  unchanged and remains unaware of Vault.
- **Secret source:** Vault's **database secrets engine** (dynamic, short-lived
  credentials)
  is the primary choice; it matches the production goal and issues per-app MariaDB users
  on demand. Lease **renewal/rotation lifecycle** is enabled. Static **KV v2** secrets
  are a documented, simpler fallback (see the config comments).
- **Connection:** `VAULT_URI` points at the remote cloud Vault over **HTTPS/TLS**.
  Authentication is **AppRole** (role-id / secret-id from the environment) in production
  and **Token** for throwaway experiments. All Vault settings are environment-driven
  (12-factor,
  [ADR-0007](0007-docker-compose-caddy-deployment.md)).
- **Local dev:** a `local` Spring profile disables Vault
  (`spring.cloud.vault.enabled=false`)
  and reads `spring.datasource.username/password` from the environment instead, so no
  Vault is needed to build or run locally.

## Consequences

- No database password ever lives in the repository, the image, or Compose files; the
  composition root acquires it at runtime. This is the main win.
- Adds Spring Cloud to the stack: the `spring-cloud-dependencies` BOM is imported in the
  root POM, and `bottify-bootstrap` gains `spring-cloud-starter-vault-config`.
- **Startup now depends on the remote Vault being reachable.** If the cloud Vault is
  down or the VPS cannot reach it, the app will not start. This is accepted for a
  single-user system; mitigate with Spring Cloud Vault's fast-fail + retry settings and
  monitoring.
- **Dynamic credentials expire and rotate.** The connection pool must cope with
  rotation — this is the classic operational subtlety of Vault dynamic DB secrets
  (leases must be renewed; a live HikariCP pool must be refreshed when creds change). If
  this proves fiddly, the KV v2 fallback removes rotation entirely at the cost of
  long-lived credentials.
- **Network & policy surface:** the VPS must reach the cloud Vault over TLS; Vault must
  be configured with a least-privilege AppRole and a database role scoped to Bottify's
  schema.
- Supersedes the "Docker secrets / env" note for **database credentials** in ADR-0007;
  other non-secret config still follows 12-factor env injection.
