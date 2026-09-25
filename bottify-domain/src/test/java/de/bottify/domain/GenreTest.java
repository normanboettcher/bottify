package de.bottify.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GenreTest {

    @Test
    void normalizes_name() {
        // when
        var genre = new Genre("  Rock  ");

        // then
        assertThat(genre.name()).isEqualTo("rock");
    }

    @Test
    void throws_on_null_name() {
        // when/then
        assertThatThrownBy(() -> new Genre(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("name must not be null");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t", "\n"})
    void throws_on_blank_name(String name) {
        // when/then
        assertThatThrownBy(() -> new Genre(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("name must not be blank");
    }

    @Test
    void hoerspiel_has_normalized_name() {
        // then
        assertThat(Genre.HOERSPIEL.name()).isEqualTo("hoerspiel");
    }
}
