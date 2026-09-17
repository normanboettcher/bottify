# ADR-0003: Build a custom API and web player instead of adopting Subsonic

- **Status:** Accepted
- **Date:** 2026-09-13

## Context

A natural option for a self-hosted music server is to implement the
[Subsonic / OpenSubsonic API](https://opensubsonic.netlify.app/). Doing so grants an
entire ecosystem of mature third-party client apps (Symfonium, DSub, Feishin, play:Sub)
for free — the server becomes one more Subsonic backend and needs no client of its own.

However, the owner's primary goal for Bottify is **learning by building**. Adopting a
prescribed API contract means implementing someone else's design rather than designing
the browse/stream/auth model themselves, and it constrains the data model and endpoints
to fit the Subsonic spec (including some legacy quirks, e.g. its recoverable-password
token scheme).

The owner explicitly chose to build the whole system themselves for deeper understanding.

## Decision

Design and implement a **custom HTTP API** shaped by Bottify's own needs, and build a
**custom web player** as its client. Do **not** implement the Subsonic API for the
initial system.

## Consequences

- **Maximum learning and full control**: the owner designs the data model, auth, and
  streaming semantics end to end, and understands every layer.
- **The web browser becomes the sole client.** The previously desired "use existing
  player apps" capability is dropped — those apps speak Subsonic and will not work
  against a custom API. Any mobile client would also have to be built in-house.
- More work: browse, search, streaming with range requests, playlists, and auth must
  all be designed and implemented rather than inherited.
- **Reversible via an adapter.** If the app ecosystem is wanted later, a
  Subsonic-compatible adapter can be added as a translation layer over the existing
  domain model, without redesigning the core. That future choice would be its own ADR.
- The API is internal and versioned by us, so it can evolve freely alongside the web
  player without third-party compatibility constraints.
