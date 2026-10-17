package de.bottify.domain.id;

import java.util.Objects;
import java.util.UUID;

/// The unique identifier of an application user.
///
/// @param value The wrapped {@link UUID}.
public record AppUserId(UUID value) implements EntityId {

    /// @throws NullPointerException if value is null.
    public AppUserId {
        Objects.requireNonNull(value, "appUserId must not be null");
    }

    /// Creates the ID from the string representation of a {@link UUID}.
    ///
    /// @throws NullPointerException     if appUserId is null.
    /// @throws IllegalArgumentException if appUserId is not a valid UUID.
    public static AppUserId fromString(String appUserId) {
        Objects.requireNonNull(appUserId, "appUserId must not be null");
        return new AppUserId(UUID.fromString(appUserId));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
