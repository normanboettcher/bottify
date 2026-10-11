package de.bottify.domain.id;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArtistIdTest {

    @Test
    void creates_artist_id_with_valid_uuid() {
        // given
        var uuid = UUID.randomUUID();

        // when
        var artistId = new ArtistId(uuid);

        // then
        assertThat(artistId.artistId()).isEqualTo(uuid);
        assertThat(artistId.toString()).isEqualTo(uuid.toString());
    }

    @Test
    void throws_on_null_uuid() {
        // when/then
        assertThatThrownBy(() -> new ArtistId(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("artistId must not be null");
    }

    @Test
    void creates_artist_id_from_string() {
        // given
        var uuid = UUID.randomUUID();

        // when
        var artistId = ArtistId.fromString(uuid.toString());

        // then
        assertThat(artistId.artistId()).isEqualTo(uuid);
        assertThat(artistId.toString()).isEqualTo(uuid.toString());
    }

    @Test
    void throws_on_null_string() {
        // when/then
        assertThatThrownBy(() -> ArtistId.fromString(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("artistId must not be null");
    }

    @Test
    void throws_on_invalid_string() {
        // when/then
        assertThatThrownBy(() -> ArtistId.fromString("not-a-uuid"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void artist_ids_with_same_uuid_are_equal() {
        // given
        var uuid = UUID.randomUUID();
        var artistId = new ArtistId(uuid);
        var otherArtistId = new ArtistId(uuid);

        // then
        assertThat(artistId).isEqualTo(otherArtistId);
        assertThat(artistId.hashCode()).isEqualTo(otherArtistId.hashCode());
    }

    @Test
    void artist_ids_with_different_uuids_are_not_equal() {
        // given
        var artistId = new ArtistId(UUID.randomUUID());
        var otherArtistId = new ArtistId(UUID.randomUUID());

        // then
        assertThat(artistId).isNotEqualTo(otherArtistId);
    }

    @Test
    void is_not_equal_to_null_or_another_type() {
        // given
        var artistId = new ArtistId(UUID.randomUUID());

        // then
        assertThat(artistId).isNotEqualTo(null).isNotEqualTo("artistId");
    }
}
