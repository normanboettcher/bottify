package de.bottify.domain;

import de.bottify.domain.id.ArtistId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArtistTest {

    @Test
    void throws_on_null_artistId() {
        // when/then
        assertThatThrownBy(() -> new Artist(null, "Test Artist", new Rating(3)))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("artistId must not be null");
    }

    @Test
    void throws_on_null_name() {
        // when/then
        assertThatThrownBy(() -> new Artist(givenArtistId(), null, new Rating(3)))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("name must not be null");
    }

    @Test
    void throws_on_null_rating() {
        // when/then
        assertThatThrownBy(() -> new Artist(givenArtistId(), "Test Artist", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("rating must not be null");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t", "\n"})
    void throws_on_blank_name(String name) {
        // when/then
        assertThatThrownBy(() -> new Artist(givenArtistId(), name, new Rating(3)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("name must not be blank");
    }

    @Test
    void creates_artist_with_valid_values() {
        // given
        var artistId = givenArtistId();
        var rating = new Rating(3);

        // when
        var artist = new Artist(artistId, "Test Artist", rating);

        // then
        assertThat(artist.artistId()).isEqualTo(artistId);
        assertThat(artist.name()).isEqualTo("Test Artist");
        assertThat(artist.rating()).isEqualTo(rating);
    }

    @Test
    void artists_with_same_id_are_equal() {
        // given
        var artistId = givenArtistId();
        var artist = new Artist(artistId, "Test Artist", new Rating(3));
        var otherArtist = new Artist(artistId, "Another Artist", new Rating(5));

        // when/then
        assertThat(artist).isEqualTo(otherArtist);
        assertThat(artist.hashCode()).isEqualTo(otherArtist.hashCode());
    }

    @Test
    void artists_with_different_ids_are_not_equal() {
        // given
        var artist = new Artist(givenArtistId(), "Test Artist", new Rating(3));
        var otherArtist = new Artist(givenArtistId(), "Test Artist", new Rating(3));

        // when/then
        assertThat(artist).isNotEqualTo(otherArtist);
    }

    @Test
    void is_not_equal_to_null_or_another_type() {
        // given
        var artist = new Artist(givenArtistId(), "Test Artist", new Rating(3));

        // when/then
        assertThat(artist).isNotEqualTo(null).isNotEqualTo("Test Artist");
    }

    private static ArtistId givenArtistId() {
        return new ArtistId(UUID.randomUUID());
    }
}
