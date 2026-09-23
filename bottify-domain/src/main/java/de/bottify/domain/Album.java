package de.bottify.domain;

import de.bottify.domain.id.AlbumId;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

/// Album record represents a music album with its associated details.
///
/// @param albumId     The unique identifier for the album, represented by {@link AlbumId}.
/// @param artist      The {@link Artist} who created the album.
/// @param title       The title of the album.
/// @param releaseDate The release date of the album in a standard format (e.g., YYYY-MM-DD).
/// @param genre       The {@link Genre} of the album (e.g., Rock, Pop, Jazz, etc.).
/// @param tracks      A {@link List} of {@link Track} objects representing the tracks in the album.
/// @param rating      The {@link Rating} of the album.
public record Album(AlbumId albumId, Artist artist, String title, String releaseDate,
                    Genre genre,
                    List<Track> tracks, Rating rating) {

    private static final int SIXTY_MINUTES_IN_SECONDS = 60;

    /// Constructor for the Album record that ensures all fields are non-null.
    ///
    /// @throws NullPointerException if any of the parameters are null.
    public Album {
        Objects.requireNonNull(artist, "artist must not be null");
        Objects.requireNonNull(title, "title must not be null");
        Objects.requireNonNull(releaseDate, "releaseDate must not be null");
        Objects.requireNonNull(genre, "genre must not be null");
        Objects.requireNonNull(rating, "rating must not be null");
        tracks = List.copyOf(Objects.requireNonNull(tracks, "tracks must not be null"));

        if (tracks.isEmpty()) {
            throw new IllegalArgumentException("tracks must not be empty");
        }
        if (title.isEmpty()) {
            throw new IllegalArgumentException("title must not be empty");
        }
        if (releaseDate.isEmpty()) {
            throw new IllegalArgumentException("releaseDate must not be empty");
        }
    }

    public int size() {
        return this.tracks.size();
    }

    /// Calculates the total length of the album in minutes by summing the durations ]
    /// of all tracks.
    ///
    /// @return The total length of the album in minutes as a double.
    public Duration totalDuration() {
        return tracks.stream().map(Track::duration).reduce(Duration.ZERO, Duration::plus);
    }
}
