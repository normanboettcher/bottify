# ADR-0002: Single-VPS modular monolith, not microservices

- **Status:** Accepted
- **Date:** 2026-09-13

## Context

Bottify serves exactly one user (the owner) from one rented Linux VPS, streaming a
small personal library (< 50 CDs, ~25 GB). The owner asked for a "production-ready,
cloud-native" architecture. That phrase is often taken to mean microservices,
Kubernetes, message brokers, and a service mesh — none of which are justified by this
workload. The real goals behind "cloud-native" here are reproducibility, observability,
and reliable operation, not horizontal scale.

## Decision

Build Bottify as a **modular monolith**: a single deployable application, internally
separated into clean modules (library scanning, streaming, transcoding, API, auth,
web player). Run it on a single VPS. Do not introduce Kubernetes, microservices, or a
message queue.

"Cloud-native" is honored through practices that fit the scale: immutable container
images, declarative configuration, 12-factor config, health checks, and one-command
reproducible deploys (see [ADR-0007](0007-docker-compose-caddy-deployment.md)).

## Consequences

- Dramatically lower operational burden: fewer moving parts to secure, patch, and debug.
- Clear internal module boundaries keep the code maintainable and make it *possible*
  (not free) to extract a service later if scale ever demanded it.
- We forgo independent scaling and deployment of components — an acceptable loss for a
  single-user app, and a poor fit for its needs anyway.
- The same container image would drop into an orchestrator unchanged if requirements
  ever changed radically.
