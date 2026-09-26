package de.bottify.domain;

import java.util.Locale;
import java.util.Objects;

/// A genre is a category of artistic composition, as in music or literature,
/// characterized by similarities in form, style, or subject matter.
public record Genre(String name) {

    public static final Genre HOERSPIEL = new Genre("Hoerspiel");

    public Genre {
        name = Objects.requireNonNull(name, "name must not be null").strip();
        if (name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
    }

    /// Case-insensitive comparison key, so tag variants like "Rock"/"rock"/"ROCK" collapse
    /// to one genre while the original capitalization survives for display.
    public String key() {
        return name.toLowerCase(Locale.ROOT);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Genre g && key().equals(g.key());
    }

    @Override
    public int hashCode() {
        return key().hashCode();
    }
}
