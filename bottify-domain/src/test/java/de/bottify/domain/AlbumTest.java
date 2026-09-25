package de.bottify.domain;

import de.bottify.domain.id.AlbumId;
import de.bottify.domain.id.ArtistId;
import de.bottify.domain.id.TrackId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AlbumTest {

    @Test
    void creates_album_with_valid_values() {
        // given
        var albumId = new AlbumId(java.util.UUID.randomUUID());
        var artistId = new ArtistId(java.util.UUID.randomUUID());
        var track = givenTrack();
        var rating = new Rating(3);

        // when
        var album = new Album(albumId, artistId, "Test Album",
                java.time.LocalDate.of(2024, 1, 1),
                Genre.HOERSPIEL,
                java.util.Collections.singletonList(track),
                rating, 1);

        // then
        assertThat(album.albumId()).isEqualTo(albumId);
        assertThat(album.artistId()).isEqualTo(artistId);
        assertThat(album.title()).isEqualTo("Test Album");
        assertThat(album.releaseDate()).isEqualTo(java.time.LocalDate.of(2024, 1, 1));
        assertThat(album.genre()).isEqualTo(Genre.HOERSPIEL);
        assertThat(album.tracks()).containsExactly(track);
        assertThat(album.rating()).isEqualTo(rating);
        assertThat(album.discNumber()).isEqualTo(1);
    }

    @Test
    void albums_with_same_id_are_equal() {
        // given
        var albumId = new AlbumId(java.util.UUID.randomUUID());
        var album = givenAlbum(albumId, "Test Album", new Rating(3));
        var otherAlbum = givenAlbum(albumId, "Another Album", new Rating(5));

        // when/then
        assertThat(album).isEqualTo(otherAlbum);
        assertThat(album.hashCode()).isEqualTo(otherAlbum.hashCode());
    }

    @Test
    void albums_with_different_ids_are_not_equal() {
        // given
        var album = givenAlbum(new AlbumId(java.util.UUID.randomUUID()), "Test Album", new Rating(3));
        var otherAlbum = givenAlbum(new AlbumId(java.util.UUID.randomUUID()), "Test Album", new Rating(3));

        // when/then
        assertThat(album).isNotEqualTo(otherAlbum);
    }

    @Test
    void is_not_equal_to_null_or_another_type() {
        // given
        var album = givenAlbum(new AlbumId(java.util.UUID.randomUUID()), "Test Album", new Rating(3));

        // when/then
        assertThat(album).isNotEqualTo(null).isNotEqualTo("Test Album");
    }

    @Test
    void throws_on_empty_tracks() {
        // given
        var albumId = new AlbumId(java.util.UUID.randomUUID());
        // when/then
        assertThatThrownBy(() -> new Album(albumId, new ArtistId(java.util.UUID.randomUUID()),
                "Test Album",
                java.time.LocalDate.now(),
                Genre.HOERSPIEL,
                java.util.Collections.emptyList(),
                new Rating(3), 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tracks must not be empty");
    }

    @Test
    void throws_on_null_tracks() {
        // given
        var albumId = new AlbumId(java.util.UUID.randomUUID());
        // when/then
        assertThatThrownBy(() -> new Album(albumId, new ArtistId(java.util.UUID.randomUUID()),
                "Test Album",
                java.time.LocalDate.now(),
                Genre.HOERSPIEL,
                null,
                new Rating(3), 1))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("tracks must not be null");
    }

    @Test
    void throws_on_null_genre() {
        // given
        var albumId = new AlbumId(java.util.UUID.randomUUID());
        // when/then
        assertThatThrownBy(() -> new Album(albumId, new ArtistId(java.util.UUID.randomUUID()),
                "Test Album",
                java.time.LocalDate.now(),
                null,
                java.util.Collections.singletonList(givenTrack()),
                new Rating(3), 1))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("genre must not be null");
    }

    @Test
    void throws_on_null_rating() {
        // given
        var albumId = new AlbumId(java.util.UUID.randomUUID());
        // when/then
        assertThatThrownBy(() -> new Album(albumId, new ArtistId(java.util.UUID.randomUUID()),
                "Test Album",
                java.time.LocalDate.now(),
                Genre.HOERSPIEL,
                java.util.Collections.singletonList(givenTrack()),
                null, 1))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("rating must not be null");
    }

    @Test
    void throws_on_null_releaseDate() {
        // given
        var albumId = new AlbumId(java.util.UUID.randomUUID());
        // when/then
        assertThatThrownBy(() -> new Album(albumId, new ArtistId(java.util.UUID.randomUUID()),
                "Test Album",
                null,
                Genre.HOERSPIEL,
                java.util.Collections.singletonList(givenTrack()),
                new Rating(3), 1))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("releaseDate must not be null");
    }

    @Test
    void throws_on_empty_title() {
        // given
        var albumId = new AlbumId(java.util.UUID.randomUUID());
        // when/then
        assertThatThrownBy(() -> new Album(albumId, new ArtistId(java.util.UUID.randomUUID()),
                "",
                java.time.LocalDate.now(),
                Genre.HOERSPIEL,
                java.util.Collections.singletonList(givenTrack()),
                new Rating(3), 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("title must not be blank");
    }

    @Test
    void throws_on_null_title() {
        // given
        var albumId = new AlbumId(java.util.UUID.randomUUID());
        // when/then
        assertThatThrownBy(() -> new Album(albumId, new ArtistId(java.util.UUID.randomUUID()),
                null,
                java.time.LocalDate.now(),
                Genre.HOERSPIEL,
                java.util.Collections.singletonList(givenTrack()),
                new Rating(3), 1))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("title must not be null");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void throws_on_invalid_discNumber(int discNumber) {
        // given
        var albumId = new AlbumId(java.util.UUID.randomUUID());
        // when/then
        assertThatThrownBy(() -> new Album(albumId, new ArtistId(java.util.UUID.randomUUID()),
                "Test Album",
                java.time.LocalDate.now(),
                Genre.HOERSPIEL,
                java.util.Collections.singletonList(givenTrack()),
                new Rating(3), discNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("discNumber must be positive");
    }

    private static Track givenTrack() {
        return new Track(new TrackId(java.util.UUID.randomUUID()), "Test Track",
                java.time.Duration.ofSeconds(180),
                new Rating(3),
                1);
    }

    private static Album givenAlbum(AlbumId albumId, String title, Rating rating) {
        return new Album(albumId, new ArtistId(java.util.UUID.randomUUID()), title,
                java.time.LocalDate.now(),
                Genre.HOERSPIEL,
                java.util.Collections.singletonList(givenTrack()),
                rating, 1);
    }
}
