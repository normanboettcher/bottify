package de.bottify.domain.id;

import java.util.UUID;

public record ArtistId(UUID artistId) {
    public ArtistId {
        if (artistId == null) {
            throw new IllegalArgumentException("ArtistId cannot be null");
        }
    }

    public static ArtistId fromString(String id) {
        return new ArtistId(UUID.fromString(id));
    }

    @Override
    public String toString() {
        return artistId.toString();
    }
}
