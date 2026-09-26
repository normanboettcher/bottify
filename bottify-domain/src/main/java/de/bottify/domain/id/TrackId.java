package de.bottify.domain.id;

import java.util.Objects;
import java.util.UUID;

public record TrackId(UUID trackId) {
    public TrackId {
        Objects.requireNonNull(trackId, "trackId must not be null");
    }

    public static TrackId fromString(String trackId) {
        Objects.requireNonNull(trackId, "trackId must not be null");
        return new TrackId(UUID.fromString(trackId));
    }

    @Override
    public String toString() {
        return trackId.toString();
    }
}
