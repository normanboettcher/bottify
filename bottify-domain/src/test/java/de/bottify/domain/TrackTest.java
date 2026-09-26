package de.bottify.domain;

import de.bottify.domain.id.TrackId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TrackTest {

    @Test
    void creates_track_with_valid_values() {
        // given
        var trackId = givenTrackId();
        var duration = Duration.ofSeconds(180);
        var rating = new Rating(3);

        // when
        var track = new Track(trackId, "Test Track", duration, rating, 1, 1);

        // then
        assertThat(track.trackId()).isEqualTo(trackId);
        assertThat(track.title()).isEqualTo("Test Track");
        assertThat(track.duration()).isEqualTo(duration);
        assertThat(track.rating()).isEqualTo(rating);
        assertThat(track.trackNumber()).isEqualTo(1);
        assertThat(track.discNumber()).isEqualTo(1);
    }

    @Test
    void throws_on_null_trackId() {
        // when/then
        assertThatThrownBy(() -> new Track(null, "Test Track", Duration.ofSeconds(180),
                new Rating(3), 1, 1))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("trackId must not be null");
    }

    @Test
    void throws_on_null_title() {
        // when/then
        assertThatThrownBy(() -> new Track(givenTrackId(), null, Duration.ofSeconds(180),
                new Rating(3), 1, 1))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("title must not be null");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t", "\n"})
    void throws_on_blank_title(String title) {
        // when/then
        assertThatThrownBy(() -> new Track(givenTrackId(), title, Duration.ofSeconds(180),
                new Rating(3), 1, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("title must not be blank");
    }

    @Test
    void throws_on_null_duration() {
        // when/then
        assertThatThrownBy(() -> new Track(givenTrackId(), "Test Track", null,
                new Rating(3), 1, 1))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("duration must not be null");
    }

    @Test
    void throws_on_negative_duration() {
        // when/then
        assertThatThrownBy(() -> new Track(givenTrackId(), "Test Track", Duration.ofSeconds(-1),
                new Rating(3), 1, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("duration must be positive");
    }

    @Test
    void throws_on_zero_duration() {
        // when/then
        assertThatThrownBy(() -> new Track(givenTrackId(), "Test Track", Duration.ZERO,
                new Rating(3), 1, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("duration must be positive");
    }

    @Test
    void throws_on_null_rating() {
        // when/then
        assertThatThrownBy(() -> new Track(givenTrackId(), "Test Track", Duration.ofSeconds(180),
                null, 1, 1))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("rating must not be null");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void throws_on_non_positive_trackNumber(int trackNumber) {
        // when/then
        assertThatThrownBy(() -> new Track(givenTrackId(), "Test Track", Duration.ofSeconds(180),
                new Rating(3), trackNumber, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("trackNumber must be greater than 0");
    }

    @Test
    void tracks_with_same_id_are_equal() {
        // given
        var trackId = givenTrackId();
        var track = new Track(trackId, "Test Track", Duration.ofSeconds(180), new Rating(3), 1, 1);
        var otherTrack = new Track(trackId, "Another Track", Duration.ofSeconds(240), new Rating(5), 2, 1);

        // then
        assertThat(track).isEqualTo(otherTrack);
        assertThat(track.hashCode()).isEqualTo(otherTrack.hashCode());
    }

    @Test
    void tracks_with_different_ids_are_not_equal() {
        // given
        var track = new Track(givenTrackId(), "Test Track", Duration.ofSeconds(180), new Rating(3), 1, 1);
        var otherTrack = new Track(givenTrackId(), "Test Track", Duration.ofSeconds(180), new Rating(3), 1, 1);

        // then
        assertThat(track).isNotEqualTo(otherTrack);
    }

    @Test
    void is_not_equal_to_null_or_another_type() {
        // given
        var track = new Track(givenTrackId(), "Test Track", Duration.ofSeconds(180), new Rating(3), 1, 1);

        // then
        assertThat(track).isNotEqualTo(null).isNotEqualTo("Test Track");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void throws_on_non_positive_discNumber(int discNumber) {
        // when/then
        assertThatThrownBy(() -> new Track(givenTrackId(), "Test Track", Duration.ofSeconds(180),
                new Rating(3), 1, discNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("discNumber must be positive");
    }

    private static TrackId givenTrackId() {
        return new TrackId(UUID.randomUUID());
    }
}
