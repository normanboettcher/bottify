# ADR-0005: Metadata in MariaDB, audio on the filesystem

- **Status:** Accepted
- **Date:** 2026-09-13

## Context

Bottify needs to store two very different kinds of data: (1) a searchable catalog —
artists, albums, tracks, playlists, users, play history — and (2) large binary audio
files that must be streamed with byte-range support. These have opposite storage needs.

A relational database is the right home for the catalog. The two mainstream open-source
choices are PostgreSQL and MariaDB; both are production-grade and first-class in Spring
Boot 4 / Hibernate / Flyway. At Bottify's scale (a few thousand rows) their performance
is effectively identical.

The main technical differentiator would be full-text search, where PostgreSQL is stronger
(built-in FTS, `pg_trgm` for fuzzy/accent-tolerant matching). Bottify, however, plans to
handle search with a **dedicated search engine (Elasticsearch) in a later phase** — partly
as a deliberate learning goal. That decision removes text search as a reason to prefer
one relational engine over the other, and it keeps search cleanly separated from the
system of record.

## Decision

Store **catalog metadata in MariaDB** (latest LTS, 11.x — pin the exact version at setup),
with schema managed by **Flyway migrations** from the first commit. Store **audio files on
the VPS filesystem** in a dedicated media directory; the database holds only file paths and
derived metadata, never audio blobs.

Full-text/fuzzy search is explicitly **not** a responsibility of MariaDB here; it is
deferred to a dedicated search service (see the architecture overview).

## Consequences

- Files can be streamed directly with efficient range requests and served/transcoded
  without pulling multi-megabyte blobs through the database.
- The database stays small and fast, and backups split cleanly: a logical dump
  (`mariadb-dump`) for metadata plus a file-level snapshot of the media directory
  (see [ADR-0007](0007-docker-compose-caddy-deployment.md)).
- Two sources of truth must be kept consistent: the library scanner reconciles the
  filesystem into the catalog and must handle files that are added, moved, or removed
  out of band.
- Flyway makes schema evolution reproducible and reviewable, at the cost of writing a
  migration for every schema change — a discipline we accept.
- MariaDB's default collations are case-insensitive, which is convenient for name lookups
  without extra work.
- Keep entities and queries in JPA/HQL and avoid vendor-specific SQL so the engine stays
  swappable; the main lock-in surface is Flyway migration syntax.
- The small library (~25 GB) fits comfortably on local VPS disk; no object storage
  (S3) is needed. If the library grew past the volume, moving audio to S3-compatible
  storage would be a new decision, not a rewrite.
