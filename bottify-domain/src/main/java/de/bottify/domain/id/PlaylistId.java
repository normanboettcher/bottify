package de.bottify.domain.id;

import java.util.Objects;
import java.util.UUID;

/// The unique identifier of a playlist.
///
/// @param value The wrapped {@link UUID}.
public record PlaylistId(UUID value) implements EntityId {

    /// @throws NullPointerException if value is null.
    public PlaylistId {
        Objects.requireNonNull(value, "playlistId must not be null");
    }

    /// Creates the ID from the string representation of a {@link UUID}.
    ///
    /// @throws NullPointerException     if playlistId is null.
    /// @throws IllegalArgumentException if playlistId is not a valid UUID.
    public static PlaylistId fromString(String playlistId) {
        Objects.requireNonNull(playlistId, "playlistId must not be null");
        return new PlaylistId(UUID.fromString(playlistId));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
