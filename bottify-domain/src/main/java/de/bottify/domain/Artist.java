package de.bottify.domain;

import de.bottify.domain.id.ArtistId;

import java.util.Objects;

/// Artist record represents a musical artist or band,
/// encapsulating their name and a collection of albums they have released.
///
/// @param artistId A unique identifier for the artist, represented by an {@link ArtistId} object.
/// @param name     The name of the artist or band.
/// @param rating   The {@link Rating} of the artist.
public record Artist(ArtistId artistId, String name, Rating rating) {
    /// Constructor for the Artist record that ensures all fields are non-null.
    ///
    /// @throws NullPointerException if any of the parameters are null.
    public Artist {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(rating, "rating must not be null");
        Objects.requireNonNull(artistId, "artistId must not be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
    }

    /// {@inheritDoc}
    ///
    /// Entity values are considered equal if they have the same {@link ArtistId}.
    @Override
    public boolean equals(Object o) {
        return o instanceof Artist other && artistId.equals(other.artistId);
    }

    /// {@inheritDoc}
    ///
    /// The hash code is based on the {@link ArtistId}.
    @Override
    public int hashCode() {
        return artistId.hashCode();
    }
}
