# ADR-0001: Record architecture decisions

- **Status:** Accepted
- **Date:** 2026-09-13

## Context

Bottify is a personal project built primarily as a learning exercise. Even a solo
project accumulates decisions ("why MariaDB?", "why not Kubernetes?") whose reasoning
is easy to forget months later. Without a record, past-me and future-me disagree, and
choices get quietly reversed without understanding why they were made.

## Decision

We will record every significant architectural decision as an Architecture Decision
Record (ADR) in the `adr/` directory, using the Michael Nygard format (Context /
Decision / Consequences).

An ADR is warranted when a decision is expensive to reverse, shapes multiple parts of
the system, or has a non-obvious rationale. Small, local implementation choices do not
need an ADR — they belong in code and the architecture overview.

## Consequences

- The reasoning behind the system is preserved and reviewable in version control.
- There is a small, deliberate overhead: a decision isn't "done" until its ADR is written.
- Changing a decision means writing a new superseding ADR, not editing history — the
  evolution of the design stays legible.
