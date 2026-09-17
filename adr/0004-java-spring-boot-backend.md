# ADR-0004: Java 25 / Spring Boot for the backend

- **Status:** Accepted
- **Date:** 2026-09-13

## Context

The backend must serve a custom HTTP API, stream audio efficiently (I/O-bound work with
HTTP range requests), read audio-file metadata, and shell out to a transcoder. The owner
is a Java / Spring Boot developer, and the project is a long-lived personal system where
familiarity and maintainability matter more than novelty.

## Decision

Build the backend on **Java 25 (the current LTS)** with **Spring Boot 4.0.x** (built on
Spring Framework 7), using Spring Web MVC for the API and Spring Security for
authentication. Use **JAudiotagger** for reading audio metadata and cover art in pure Java.

## Consequences

- Plays to the owner's existing strength — faster progress, fewer unknowns, better code.
- Virtual threads (stable since Java 21, matured further by 25) suit the streaming
  workload well: many concurrent, mostly-idle I/O connections without thread-pool tuning
  gymnastics.
- Staying on the current LTS and the matching major Spring Boot release maximizes the
  supported lifetime of the stack and access to modern language features.
- Spring Boot brings batteries-included config, security, health checks, and metrics,
  which directly serve the production-readiness goals.
- Heavier runtime footprint than a Go/Rust binary, but well within a modest VPS for a
  single user; the container image is kept lean with a multi-stage build
  (see [ADR-0007](0007-docker-compose-caddy-deployment.md)).
