package de.bottify.domain.id;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AlbumIdTest {

    @Test
    void creates_album_id_with_valid_uuid() {
        // given
        var uuid = UUID.randomUUID();

        // when
        var albumId = new AlbumId(uuid);

        // then
        assertThat(albumId.albumId()).isEqualTo(uuid);
        assertThat(albumId.toString()).isEqualTo(uuid.toString());
    }

    @Test
    void throws_on_null_uuid() {
        // when/then
        assertThatThrownBy(() -> new AlbumId(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("albumId must not be null");
    }

    @Test
    void creates_album_id_from_string() {
        // given
        var uuid = UUID.randomUUID();

        // when
        var albumId = AlbumId.fromString(uuid.toString());

        // then
        assertThat(albumId.albumId()).isEqualTo(uuid);
        assertThat(albumId.toString()).isEqualTo(uuid.toString());
    }

    @Test
    void throws_on_null_string() {
        // when/then
        assertThatThrownBy(() -> AlbumId.fromString(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("albumId must not be null");
    }

    @Test
    void throws_on_invalid_string() {
        // when/then
        assertThatThrownBy(() -> AlbumId.fromString("not-a-uuid"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void album_ids_with_same_uuid_are_equal() {
        // given
        var uuid = UUID.randomUUID();
        var albumId = new AlbumId(uuid);
        var otherAlbumId = new AlbumId(uuid);

        // then
        assertThat(albumId).isEqualTo(otherAlbumId);
        assertThat(albumId.hashCode()).isEqualTo(otherAlbumId.hashCode());
    }

    @Test
    void album_ids_with_different_uuids_are_not_equal() {
        // given
        var albumId = new AlbumId(UUID.randomUUID());
        var otherAlbumId = new AlbumId(UUID.randomUUID());

        // then
        assertThat(albumId).isNotEqualTo(otherAlbumId);
    }

    @Test
    void is_not_equal_to_null_or_another_type() {
        // given
        var albumId = new AlbumId(UUID.randomUUID());

        // then
        assertThat(albumId).isNotEqualTo(null).isNotEqualTo("albumId");
    }
}
