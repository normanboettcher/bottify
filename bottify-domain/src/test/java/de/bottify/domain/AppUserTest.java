package de.bottify.domain;

import de.bottify.domain.id.AppUserId;
import de.bottify.domain.id.PlaylistId;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AppUserTest {

    @Test
    void throws_on_null_appUserId() {
        // when/then
        assertThatThrownBy(() -> new AppUser(null, "Test User", List.of()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("appUserId must not be null");
    }

    @Test
    void throws_on_null_username() {
        // when/then
        assertThatThrownBy(() -> new AppUser(new AppUserId(UUID.randomUUID()), null, List.of()))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("username must not be null");
    }

    @Test
    void throws_on_blank_username() {
        // when/then
        assertThatThrownBy(() -> new AppUser(new AppUserId(UUID.randomUUID()), " ", List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("username must not be blank");
    }

    @Test
    void removes_whitespace_from_username() {
        // given
        var appUserId = new AppUserId(UUID.randomUUID());
        var username = "  Test User  ";

        // when
        var appUser = new AppUser(appUserId, username, List.of());

        // then
        assertThat(appUser.username()).isEqualTo("Test User");
    }

    @Test
    void creates_app_user_with_valid_values() {
        // given
        var appUserId = new AppUserId(UUID.randomUUID());
        var username = "Test User";

        // when
        var appUser = new AppUser(appUserId, username, List.of());

        // then
        assertThat(appUser.appUserId()).isEqualTo(appUserId);
        assertThat(appUser.username()).isEqualTo(username);
    }

    @Test
    void equality_by_appUserId() {
        // given
        var appUserId = new AppUserId(UUID.randomUUID());
        var appUser1 = new AppUser(appUserId, "Test User", List.of());
        var appUser2 = new AppUser(appUserId, "Test User", List.of());

        // then
        assertThat(appUser1).isEqualTo(appUser2);
    }

    @Test
    void equality_by_hash_code() {
        // given
        var appUserId = new AppUserId(UUID.randomUUID());
        var appUser1 = new AppUser(appUserId, "Test User", List.of());
        var appUser2 = new AppUser(appUserId, "Test User", List.of());

        // then
        assertThat(appUser1).hasSameHashCodeAs(appUser2);
    }

    @Test
    void app_users_with_different_ids_are_not_equal() {
        // given
        var appUser1 = new AppUser(new AppUserId(UUID.randomUUID()), "Test User", List.of());
        var appUser2 = new AppUser(new AppUserId(UUID.randomUUID()), "Test User", List.of());

        // then
        assertThat(appUser1).isNotEqualTo(appUser2);
    }

    @Test
    void playlists_are_immutable() {
        // given
        var appUser = new AppUser(new AppUserId(UUID.randomUUID()), "Test User", List.of());

        // when/then
        assertThatThrownBy(() -> appUser.playlists().add(new PlaylistId(UUID.randomUUID())))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}