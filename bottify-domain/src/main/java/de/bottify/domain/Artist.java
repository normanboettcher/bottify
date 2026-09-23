package de.bottify.domain;

import de.bottify.domain.id.ArtistId;

import java.util.Objects;
import java.util.Set;

/// Artist record represents a musical artist or band,
/// encapsulating their name and a collection of albums they have released.
///
/// @param artistId A unique identifier for the artist, represented by an {@link ArtistId} object.
/// @param name     The name of the artist or band.
/// @param albums   A {@link Set} of {@link Album} objects representing the albums
///               released by the artist.
/// @param rating   The {@link Rating} of the artist.
public record Artist(ArtistId artistId, String name, Set<Album> albums, Rating rating) {
    /// Constructor for the Artist record that ensures all fields are non-null.
    ///
    /// @throws NullPointerException if any of the parameters are null.
    public Artist {
        albums = Set.copyOf(Objects.requireNonNull(albums, "albums must not be null"));
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(rating, "rating must not be null");
        Objects.requireNonNull(artistId, "artistId must not be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
    }
}
