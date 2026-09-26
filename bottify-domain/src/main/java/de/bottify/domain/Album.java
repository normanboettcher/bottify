package de.bottify.domain;

import de.bottify.domain.id.AlbumId;
import de.bottify.domain.id.ArtistId;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/// Album record represents a music album with its associated details.
///
/// @param albumId     The unique identifier for the album, represented by {@link AlbumId}.
/// @param artistId    The {@link ArtistId} of the artist who created the album.
/// @param title       The title of the album.
/// @param releaseDate The release date of the album in a standard format.
/// @param genre       The {@link Genre} of the album (e.g., Rock, Pop, Jazz, etc.).
/// @param tracks      A {@link List} of {@link Track} objects representing the tracks in the album.
///                    Can be empty if the album has no tracks or tracks are added later.
/// @param rating      The {@link Rating} of the album.
public record Album(AlbumId albumId, ArtistId artistId, String title,
                    LocalDate releaseDate,
                    Genre genre,
                    List<Track> tracks, Rating rating) {

    /// Constructor for the Album record that ensures all fields are non-null.
    ///
    /// @throws NullPointerException     if any of the parameters are null.
    /// @throws IllegalArgumentException if the title is blank.
    public Album {
        Objects.requireNonNull(artistId, "artistId must not be null");
        Objects.requireNonNull(title, "title must not be null");
        Objects.requireNonNull(genre, "genre must not be null");
        Objects.requireNonNull(rating, "rating must not be null");
        Objects.requireNonNull(releaseDate, "releaseDate must not be null");
        Objects.requireNonNull(albumId, "albumId must not be null");
        tracks = List.copyOf(Objects.requireNonNull(tracks, "tracks must not be null"));

        if (title.isBlank()) {
            throw new IllegalArgumentException("title must not be blank");
        }
    }

    public int trackCount() {
        return this.tracks.size();
    }

    /// Calculates the total length of the album  by summing the durations of all tracks.
    ///
    /// @return The total length of the album.
    public Duration totalDuration() {
        return tracks.stream().map(Track::duration).reduce(Duration.ZERO, Duration::plus);
    }

    /// {@inheritDoc}
    ///
    /// Entity values are considered equal if they have the same {@link AlbumId}.
    @Override
    public boolean equals(Object o) {
        return o instanceof Album other && albumId.equals(other.albumId);
    }

    /// {@inheritDoc}
    ///
    /// The hash code is based on the {@link AlbumId}.
    @Override
    public int hashCode() {
        return albumId.hashCode();
    }
}
