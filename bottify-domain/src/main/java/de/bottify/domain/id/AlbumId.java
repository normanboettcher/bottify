package de.bottify.domain.id;

import java.util.Objects;
import java.util.UUID;

public record AlbumId(UUID albumId) {
    public AlbumId {
        Objects.requireNonNull(albumId, "albumId must not be null");
    }

    public static AlbumId fromString(String id) {
        return new AlbumId(UUID.fromString(id));
    }

    @Override
    public String toString() {
        return albumId.toString();
    }
}
