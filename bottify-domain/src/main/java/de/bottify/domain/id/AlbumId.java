package de.bottify.domain.id;

import java.util.Objects;
import java.util.UUID;

/// The unique identifier of an album.
///
/// @param value The wrapped {@link UUID}.
public record AlbumId(UUID value) implements EntityId {

    /// @throws NullPointerException if value is null.
    public AlbumId {
        Objects.requireNonNull(value, "albumId must not be null");
    }

    /// Creates the ID from the string representation of a {@link UUID}.
    ///
    /// @throws NullPointerException     if albumId is null.
    /// @throws IllegalArgumentException if albumId is not a valid UUID.
    public static AlbumId fromString(String albumId) {
        Objects.requireNonNull(albumId, "albumId must not be null");
        return new AlbumId(UUID.fromString(albumId));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
