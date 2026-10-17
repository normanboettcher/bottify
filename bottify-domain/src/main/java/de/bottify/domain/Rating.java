package de.bottify.domain;

/// Rating record represents a rating value for an artist, album, or track.
///
/// @param value The rating value, which must be between 0 and 5 (inclusive).
///              A rating of 0 indicates no rating, while a rating of 5 indicates the highest rating.
public record Rating(int value) implements Comparable<Rating> {
    public static final int MIN = 0;
    public static final int MAX = 5;

    /// Constructor for the Rating record that ensures the value is within a
    /// valid range (0 to 5).
    ///
    /// @throws IllegalArgumentException if the value is not between 0 and 5.
    public Rating {
        if (value < MIN || value > MAX) {
            throw new IllegalArgumentException("Rating value must be between ["
                    + MIN + "] and [" + MAX + "], but was [" + value + "]");
        }
    }

    @Override
    public int compareTo(Rating rating) {
        return Integer.compare(this.value, rating.value);
    }
}
