package de.bottify.domain.id;

import java.util.Objects;
import java.util.UUID;

public record ArtistId(UUID artistId) {
    public ArtistId {
        Objects.requireNonNull(artistId, "artistId must not be null");
    }

    public static ArtistId fromString(String artistId) {
        Objects.requireNonNull(artistId, "artistId must not be null");
        return new ArtistId(UUID.fromString(artistId));
    }

    @Override
    public String toString() {
        return artistId.toString();
    }
}
