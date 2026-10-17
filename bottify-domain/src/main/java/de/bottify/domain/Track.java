package de.bottify.domain;

import de.bottify.domain.id.TrackId;

import java.time.Duration;
import java.util.Comparator;
import java.util.Objects;

/// Track record represents a single track in an album,
/// encapsulating its title and duration in seconds.
///
/// @param trackId     The unique identifier for the track, represented by {@link TrackId}.
/// @param title       The title of the track.
/// @param duration    The {@link Duration} of the track in seconds.
/// @param rating      The {@link Rating} of the track.
/// @param trackNumber The track number in the album.
/// @param discNumber  Which disc of this release is this track on (e.g. a 3-disc-box)
public record Track(TrackId trackId, String title, Duration duration,
                    Rating rating, int trackNumber,
                    int discNumber) {

    /// Orders tracks by disc, then track number, with the identifier as a final
    /// tiebreaker so the ordering is total and deterministic for mis-tagged rips
    /// that share a disc/track number.
    public static final Comparator<Track> BY_POSITION = Comparator
            .comparingInt(Track::discNumber)
            .thenComparingInt(Track::trackNumber)
            .thenComparing(t -> t.trackId().value());

    /// Constructor for the Track record that ensures all fields are non-null and valid.
    ///
    /// @throws NullPointerException     if any of the parameters are null.
    /// @throws IllegalArgumentException if the duration is negative or zero,
    ///                                  if the title is blank, or if the track number
    ///                                  or disc number are less than 1.
    public Track {
        Objects.requireNonNull(title, "title must not be null");
        Objects.requireNonNull(duration, "duration must not be null");
        Objects.requireNonNull(trackId, "trackId must not be null");
        Objects.requireNonNull(rating, "rating must not be null");

        if (title.isBlank()) {
            throw new IllegalArgumentException("title must not be blank");
        }
        if (trackNumber < 1) {
            throw new IllegalArgumentException("trackNumber must be greater than 0");
        }
        if (discNumber < 1) {
            throw new IllegalArgumentException("discNumber must be positive");
        }
        if (duration.isNegative() || duration.isZero()) {
            throw new IllegalArgumentException("duration must be positive");
        }
    }

    /// {@inheritDoc}
    ///
    /// Entity values are considered equal if they have the same {@link TrackId}.
    @Override
    public boolean equals(Object o) {
        return o instanceof Track other && trackId.equals(other.trackId);
    }

    /// {@inheritDoc}
    ///
    /// The hash code is based on the {@link TrackId}.
    @Override
    public int hashCode() {
        return trackId.hashCode();
    }
}
