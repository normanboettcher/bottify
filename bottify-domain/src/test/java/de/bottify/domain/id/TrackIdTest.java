package de.bottify.domain.id;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TrackIdTest {

    @Test
    void creates_track_id_with_valid_uuid() {
        // given
        var uuid = UUID.randomUUID();

        // when
        var trackId = new TrackId(uuid);

        // then
        assertThat(trackId.trackId()).isEqualTo(uuid);
        assertThat(trackId.toString()).isEqualTo(uuid.toString());
    }

    @Test
    void throws_on_null_uuid() {
        // when/then
        assertThatThrownBy(() -> new TrackId(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("trackId must not be null");
    }

    @Test
    void creates_track_id_from_string() {
        // given
        var uuid = UUID.randomUUID();

        // when
        var trackId = TrackId.fromString(uuid.toString());

        // then
        assertThat(trackId.trackId()).isEqualTo(uuid);
        assertThat(trackId.toString()).isEqualTo(uuid.toString());
    }

    @Test
    void throws_on_null_string() {
        // when/then
        assertThatThrownBy(() -> TrackId.fromString(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("trackId must not be null");
    }

    @Test
    void throws_on_invalid_string() {
        // when/then
        assertThatThrownBy(() -> TrackId.fromString("not-a-uuid"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void track_ids_with_same_uuid_are_equal() {
        // given
        var uuid = UUID.randomUUID();
        var trackId = new TrackId(uuid);
        var otherTrackId = new TrackId(uuid);

        // then
        assertThat(trackId).isEqualTo(otherTrackId);
        assertThat(trackId.hashCode()).isEqualTo(otherTrackId.hashCode());
    }

    @Test
    void track_ids_with_different_uuids_are_not_equal() {
        // given
        var trackId = new TrackId(UUID.randomUUID());
        var otherTrackId = new TrackId(UUID.randomUUID());

        // then
        assertThat(trackId).isNotEqualTo(otherTrackId);
    }

    @Test
    void is_not_equal_to_null_or_another_type() {
        // given
        var trackId = new TrackId(UUID.randomUUID());

        // then
        assertThat(trackId).isNotEqualTo(null).isNotEqualTo("trackId");
    }
}
