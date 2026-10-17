package de.bottify.domain;

import de.bottify.domain.id.AppUserId;
import de.bottify.domain.id.PlaylistId;

import java.util.List;
import java.util.Objects;

/// Represents an application user with a unique identifier and a username.
///
/// @param appUserId The unique identifier for the application user.
/// @param username  The username of the application user.
/// @param playlists The list of playlists associated with the application user.
public record AppUser(
        AppUserId appUserId,
        String username,
        List<PlaylistId> playlists
) {
    /// @throws NullPointerException if appUserId or username is null.
    public AppUser {
        Objects.requireNonNull(appUserId, "appUserId must not be null");
        username = Objects.requireNonNull(username, "username must not be null").strip();
        playlists = List.copyOf(Objects.requireNonNull(playlists, "playlists must not be null"));

        if (username.isBlank()) {
            throw new IllegalArgumentException("username must not be blank");
        }
    }

    /// {@inheritDoc}
    ///
    /// Equality is based on the appUserId, which is unique for each user.
    @Override
    public boolean equals(Object o) {
        return o instanceof AppUser && ((AppUser) o).appUserId().equals(appUserId);
    }

    /// {@inheritDoc}
    ///
    /// The hash code is based on the appUserId, which is unique for each user.
    @Override
    public int hashCode() {
        return appUserId.hashCode();
    }
}
