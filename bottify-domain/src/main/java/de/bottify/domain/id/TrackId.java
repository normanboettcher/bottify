package de.bottify.domain.id;

import java.util.Objects;
import java.util.UUID;

/// The unique identifier of a track.
///
/// @param value The wrapped {@link UUID}.
public record TrackId(UUID value) implements EntityId {

    /// @throws NullPointerException if value is null.
    public TrackId {
        Objects.requireNonNull(value, "trackId must not be null");
    }

    /// Creates the ID from the string representation of a {@link UUID}.
    ///
    /// @throws NullPointerException     if trackId is null.
    /// @throws IllegalArgumentException if trackId is not a valid UUID.
    public static TrackId fromString(String trackId) {
        Objects.requireNonNull(trackId, "trackId must not be null");
        return new TrackId(UUID.fromString(trackId));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
