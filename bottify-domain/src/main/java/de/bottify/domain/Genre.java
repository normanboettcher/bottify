package de.bottify.domain;

import java.util.Locale;
import java.util.Objects;

public record Genre(String name) {
    public Genre {
        name = Objects.requireNonNull(name, "name must not be null").strip().toLowerCase(Locale.ROOT);
        if (name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
    }

    public static final Genre HOERSPIEL = new Genre("Hoerspiel");
}
