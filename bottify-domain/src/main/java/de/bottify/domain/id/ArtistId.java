package de.bottify.domain.id;

import java.util.Objects;
import java.util.UUID;

/// The unique identifier of an artist.
///
/// @param value The wrapped {@link UUID}.
public record ArtistId(UUID value) implements EntityId {

    /// @throws NullPointerException if value is null.
    public ArtistId {
        Objects.requireNonNull(value, "artistId must not be null");
    }

    /// Creates the ID from the string representation of a {@link UUID}.
    ///
    /// @throws NullPointerException     if artistId is null.
    /// @throws IllegalArgumentException if artistId is not a valid UUID.
    public static ArtistId fromString(String artistId) {
        Objects.requireNonNull(artistId, "artistId must not be null");
        return new ArtistId(UUID.fromString(artistId));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
