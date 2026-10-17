package de.bottify.domain.id;

import java.util.UUID;

/// Common type of all typed entity identifiers in the domain.
///
/// Every ID is a record wrapping exactly one {@link UUID}, exposed as {@link #value()}.
/// The interface is sealed, so the set of ID types is closed and known at compile time:
/// adapters can handle all of them in one place (e.g. a single converter), and the
/// compiler rejects an ID that implements this interface without being listed here.
public sealed interface EntityId permits AlbumId, AppUserId, ArtistId, PlaylistId, TrackId {

    /// Returns the wrapped {@link UUID}.
    UUID value();
}
