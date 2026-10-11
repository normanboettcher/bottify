package de.bottify.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GenreTest {

    @ParameterizedTest
    @ValueSource(strings = {"Rock", "Pop", "Hip-Hop", "Jazz", "Classical"})
    void should_not_manipulate_genre_names(String name) {
        // then
        assertThat(new Genre(name))
                .extracting(Genre::name)
                .isEqualTo(name);
    }

    @Test
    void should_normalize_name() {
        // given
        var genre = new Genre("  Rock  ");

        // then
        assertThat(genre.name()).isEqualTo("Rock");
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
    void equality_by_key() {
        // given
        var genre = new Genre("ROCK");
        var genre2 = new Genre("rock");

        // then
        assertThat(genre).isEqualTo(genre2);
    }

    @Test
    void equality_by_hash_code() {
        // given
        var genre = new Genre("ROCK");
        var genre2 = new Genre("rock");

        // then
        assertThat(genre).hasSameHashCodeAs(genre2);
    }

    @Test
    void collapses_case_variants_into_one_set_element() {
        assertThat(new HashSet<>(List.of(new Genre("ROCK"),
                new Genre("rock"),
                new Genre("Pop"))))
                .hasSize(2);
    }
}
