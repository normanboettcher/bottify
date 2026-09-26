package de.bottify.domain;

import de.bottify.domain.id.AlbumId;
import de.bottify.domain.id.ArtistId;
import de.bottify.domain.id.TrackId;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AlbumTest {

    @Test
    void returns_track_count() {
        // given
        var tracks = List.of(
                givenTrack(java.time.Duration.ofSeconds(120), 1),
                givenTrack(java.time.Duration.ofSeconds(180), 2),
                givenTrack(java.time.Duration.ofSeconds(240), 3));
        var album = givenAlbum(new AlbumId(randomUUID()), "Test Album", new Rating(3), tracks);

        // when/then
        assertThat(album.trackCount()).isEqualTo(3);
    }

    @Test
    void returns_total_duration_of_all_tracks() {
        // given
        var tracks = List.of(
                givenTrack(java.time.Duration.ofSeconds(120), 1),
                givenTrack(java.time.Duration.ofSeconds(180), 2),
                givenTrack(java.time.Duration.ofSeconds(240), 3));
        var album = givenAlbum(new AlbumId(randomUUID()), "Test Album", new Rating(3), tracks);

        // when/then
        assertThat(album.totalDuration()).isEqualTo(java.time.Duration.ofSeconds(540));
    }

    @Test
    void creates_album_with_valid_values() {
        // given
        var albumId = new AlbumId(randomUUID());
        var artistId = new ArtistId(randomUUID());
        var track = givenTrack();
        var rating = new Rating(3);

        // when
        var album = new Album(albumId, artistId, "Test Album",
                LocalDate.of(2024, 1, 1),
                Genre.HOERSPIEL,
                List.of(track),
                rating);

        // then
        assertThat(album.albumId()).isEqualTo(albumId);
        assertThat(album.artistId()).isEqualTo(artistId);
        assertThat(album.title()).isEqualTo("Test Album");
        assertThat(album.releaseDate()).isEqualTo(java.time.LocalDate.of(2024, 1, 1));
        assertThat(album.genre()).isEqualTo(Genre.HOERSPIEL);
        assertThat(album.tracks()).containsExactly(track);
        assertThat(album.rating()).isEqualTo(rating);
    }

    @Test
    void albums_with_same_id_are_equal() {
        // given
        var albumId = new AlbumId(randomUUID());
        var album = givenAlbum(albumId, "Test Album", new Rating(3));
        var otherAlbum = givenAlbum(albumId, "Another Album", new Rating(5));

        // when/then
        assertThat(album).isEqualTo(otherAlbum);
        assertThat(album.hashCode()).isEqualTo(otherAlbum.hashCode());
    }

    @Test
    void albums_with_different_ids_are_not_equal() {
        // given
        var album = givenAlbum(new AlbumId(randomUUID()), "Test Album", new Rating(3));
        var otherAlbum = givenAlbum(new AlbumId(randomUUID()), "Test Album", new Rating(3));

        // when/then
        assertThat(album).isNotEqualTo(otherAlbum);
    }

    @Test
    void is_not_equal_to_null_or_another_type() {
        // given
        var album = givenAlbum(new AlbumId(randomUUID()), "Test Album", new Rating(3));

        // when/then
        assertThat(album).isNotEqualTo(null).isNotEqualTo("Test Album");
    }

    @Test
    void throws_on_null_albumId() {
        // when/then
        assertThatThrownBy(() -> new Album(null, new ArtistId(randomUUID()),
                "Test Album",
                LocalDate.now(),
                Genre.HOERSPIEL,
                List.of(givenTrack()),
                new Rating(3)))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("albumId must not be null");
    }

    @Test
    void throws_on_null_tracks() {
        // given
        var albumId = new AlbumId(randomUUID());
        // when/then
        assertThatThrownBy(() -> new Album(albumId, new ArtistId(randomUUID()),
                "Test Album",
                LocalDate.now(),
                Genre.HOERSPIEL,
                null,
                new Rating(3)))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("tracks must not be null");
    }

    @Test
    void throws_on_null_genre() {
        // given
        var albumId = new AlbumId(randomUUID());
        // when/then
        assertThatThrownBy(() -> new Album(albumId, new ArtistId(randomUUID()),
                "Test Album",
                LocalDate.now(),
                null,
                List.of(givenTrack()),
                new Rating(3)))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("genre must not be null");
    }

    @Test
    void throws_on_null_rating() {
        // given
        var albumId = new AlbumId(randomUUID());
        // when/then
        assertThatThrownBy(() -> new Album(albumId, new ArtistId(randomUUID()),
                "Test Album",
                LocalDate.now(),
                Genre.HOERSPIEL,
                List.of(givenTrack()),
                null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("rating must not be null");
    }

    @Test
    void throws_on_null_releaseDate() {
        // given
        var albumId = new AlbumId(randomUUID());
        // when/then
        assertThatThrownBy(() -> new Album(albumId, new ArtistId(randomUUID()),
                "Test Album",
                null,
                Genre.HOERSPIEL,
                List.of(givenTrack()),
                new Rating(3)))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("releaseDate must not be null");
    }

    @Test
    void throws_on_empty_title() {
        // given
        var albumId = new AlbumId(randomUUID());
        // when/then
        assertThatThrownBy(() -> new Album(albumId, new ArtistId(randomUUID()),
                "",
                LocalDate.now(),
                Genre.HOERSPIEL,
                List.of(givenTrack()),
                new Rating(3)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("title must not be blank");
    }

    @Test
    void throws_on_null_title() {
        // given
        var albumId = new AlbumId(randomUUID());
        // when/then
        assertThatThrownBy(() -> new Album(albumId, new ArtistId(randomUUID()),
                null,
                LocalDate.now(),
                Genre.HOERSPIEL,
                List.of(givenTrack()),
                new Rating(3)))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("title must not be null");
    }

    @Test
    void tracks_accessor_is_unmodifiable() {
        // given
        var albumId = new AlbumId(randomUUID());
        var album = givenAlbum(albumId, "Test Album", new Rating(3));

        // when/then
        assertThatThrownBy(() -> album.tracks().add(givenTrack()))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void tracks_are_copied_defensively_from_the_source_list() {
        // given
        var source = new ArrayList<Track>(List.of(givenTrack()));
        var album = new Album(new AlbumId(randomUUID()),
                new ArtistId(randomUUID()),
                "Test Album",
                LocalDate.now(),
                Genre.HOERSPIEL,
                source,
                new Rating(3));
        // when
        source.add(givenTrack());
        // then
        assertThat(album.trackCount()).isEqualTo(1);
    }

    @Test
    void track_count_on_empty_tracks() {
        // given
        var album = new Album(new AlbumId(randomUUID()),
                new ArtistId(randomUUID()),
                "Test Album",
                LocalDate.now(),
                Genre.HOERSPIEL,
                List.of(),
                new Rating(3));

        // when/then
        assertThat(album.trackCount()).isEqualTo(0);
    }

    @Test
    void duration_on_empty_track_list() {
        // given
        var album = new Album(new AlbumId(randomUUID()),
                new ArtistId(randomUUID()),
                "Test Album",
                LocalDate.now(),
                Genre.HOERSPIEL,
                List.of(),
                new Rating(3));

        // when/then
        assertThat(album.totalDuration()).isEqualTo(Duration.ZERO);
    }

    private static Track givenTrack() {
        return new Track(new TrackId(randomUUID()), "Test Track",
                Duration.ofSeconds(180),
                new Rating(3),
                1, 1);
    }

    private static Album givenAlbum(AlbumId albumId, String title, Rating rating) {
        return givenAlbum(albumId, title, rating, java.util.Collections.singletonList(givenTrack()));
    }

    private static Album givenAlbum(AlbumId albumId, String title, Rating rating, List<Track> tracks) {
        return new Album(albumId, new ArtistId(randomUUID()), title,
                LocalDate.now(),
                Genre.HOERSPIEL,
                tracks,
                rating);
    }

    private static Track givenTrack(java.time.Duration duration, int trackNumber) {
        return new Track(new TrackId(randomUUID()), "Test Track",
                duration,
                new Rating(3),
                trackNumber, 1);
    }
}
