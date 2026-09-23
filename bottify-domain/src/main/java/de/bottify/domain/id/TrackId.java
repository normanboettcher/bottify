package de.bottify.domain.id;

import java.util.UUID;

public record TrackId(UUID id) {
    public TrackId {
        if (id == null) {
            throw new IllegalArgumentException("TrackId cannot be null");
        }
    }

    public static TrackId fromString(String id) {
        return new TrackId(UUID.fromString(id));
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
