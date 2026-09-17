# ADR-0006: Store lossless, transcode on the fly

- **Status:** Accepted
- **Date:** 2026-09-13

## Context

The source material is ripped from CDs, so lossless audio (FLAC) is available at import.
Two things are in tension: preserving original quality (argues for keeping lossless) and
delivering something small and universally playable over a network to a browser (argues
for a compressed format). Storage is not a constraint at this scale (~25 GB total).

## Decision

Store the **original lossless FLAC** files as the canonical copy. **Transcode on the fly**
per request using the **ffmpeg** binary, choosing the output format/bitrate from what the
client asks for. Prefer **direct play** of the FLAC when the client and network support it;
transcode only when needed. Cache transcoded output on disk, keyed by
`(track, format, bitrate)`, to avoid re-encoding the same request repeatedly.

## Consequences

- The archival copy is always full quality; delivery format is a runtime decision, not a
  permanent one — new formats/bitrates need no re-import.
- Transcoding is CPU-bound. For a single listener this is fine on a modest VPS (~2 vCPU),
  but it must be bounded: cache results and apply resource limits so a transcode cannot
  starve the box (see [ADR-0002](0002-single-vps-modular-monolith.md)).
- ffmpeg is an external process dependency; it is baked into the application container
  image so the runtime is self-contained and reproducible.
- Slightly more complex streaming code than serving files as-is, in exchange for quality
  and client flexibility.
