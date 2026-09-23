package de.bottify.domain.id;

import java.util.UUID;

public record AlbumId(UUID id) {
    public AlbumId {
        if (id == null) {
            throw new IllegalArgumentException("AlbumId cannot be null");
        }
    }

    public static AlbumId fromString(String id) {
        return new AlbumId(UUID.fromString(id));
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
