package de.bottify.domain.id;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/// Shared contract for all [EntityId] implementations.
///
/// To cover a new ID type, add one line to [#idTypes()].
/// [#every_entity_id_is_registered()] fails if you forget.
class IdContractTest {

    static Stream<IdType<?>> idTypes() {
        return Stream.of(
                IdType.of(AlbumId.class, AlbumId::new, AlbumId::fromString),
                IdType.of(ArtistId.class, ArtistId::new, ArtistId::fromString),
                IdType.of(TrackId.class, TrackId::new, TrackId::fromString),
                IdType.of(AppUserId.class, AppUserId::new, AppUserId::fromString),
                IdType.of(PlaylistId.class, PlaylistId::new, PlaylistId::fromString)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("idTypes")
    <T extends EntityId> void creates_id_with_valid_uuid(IdType<T> idType) {
        // given
        var uuid = UUID.randomUUID();

        // when
        var id = idType.constructor().apply(uuid);

        // then
        assertThat(id.value()).isEqualTo(uuid);
        assertThat(id.toString()).isEqualTo(uuid.toString());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("idTypes")
    <T extends EntityId> void throws_on_null_uuid(IdType<T> idType) {
        // when/then
        assertThatThrownBy(() -> idType.constructor().apply(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(idType.nullMessage());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("idTypes")
    <T extends EntityId> void creates_id_from_string(IdType<T> idType) {
        // given
        var uuid = UUID.randomUUID();

        // when
        var id = idType.fromString().apply(uuid.toString());

        // then
        assertThat(id.value()).isEqualTo(uuid);
        assertThat(id.toString()).isEqualTo(uuid.toString());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("idTypes")
    <T extends EntityId> void round_trips_through_to_string(IdType<T> idType) {
        // given
        var id = idType.constructor().apply(UUID.randomUUID());

        // when
        var parsed = idType.fromString().apply(id.toString());

        // then
        assertThat(parsed).isEqualTo(id);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("idTypes")
    <T extends EntityId> void throws_on_null_string(IdType<T> idType) {
        // when/then
        assertThatThrownBy(() -> idType.fromString().apply(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(idType.nullMessage());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("idTypes")
    <T extends EntityId> void throws_on_invalid_string(IdType<T> idType) {
        // when/then
        assertThatThrownBy(() -> idType.fromString().apply("not-a-uuid"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("idTypes")
    <T extends EntityId> void ids_with_same_uuid_are_equal(IdType<T> idType) {
        // given
        var uuid = UUID.randomUUID();
        var id = idType.constructor().apply(uuid);
        var otherId = idType.constructor().apply(uuid);

        // then
        assertThat(id).isEqualTo(otherId);
        assertThat(id.hashCode()).isEqualTo(otherId.hashCode());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("idTypes")
    <T extends EntityId> void ids_with_different_uuids_are_not_equal(IdType<T> idType) {
        // given
        var id = idType.constructor().apply(UUID.randomUUID());
        var otherId = idType.constructor().apply(UUID.randomUUID());

        // then
        assertThat(id).isNotEqualTo(otherId);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("idTypes")
    <T extends EntityId> void is_not_equal_to_null_or_its_raw_uuid(IdType<T> idType) {
        // given
        var id = idType.constructor().apply(UUID.randomUUID());

        // then
        assertThat(id).isNotEqualTo(null).isNotEqualTo(id.value());
    }

    @Test
    void every_entity_id_is_registered() {
        // given
        List<Class<?>> registered = idTypes().<Class<?>>map(IdType::type).toList();

        // when
        var permitted = EntityId.class.getPermittedSubclasses();

        // then
        assertThat(registered)
                .as("Every EntityId implementation must be registered in IdContractTest.idTypes()")
                .containsExactlyInAnyOrder(permitted);
    }

    /// Describes one ID type for the contract tests.
    ///
    /// The expected null-check message is derived from the type by convention: the simple
    /// class name with a lower-case first letter (`AlbumId` -> `"albumId must not be null"`).
    record IdType<T extends EntityId>(
            Class<T> type,
            Function<UUID, T> constructor,
            Function<String, T> fromString
    ) {
        static <T extends EntityId> IdType<T> of(
                Class<T> type, Function<UUID, T> constructor, Function<String, T> fromString) {
            return new IdType<>(type, constructor, fromString);
        }

        String fieldName() {
            var name = type.getSimpleName();
            return Character.toLowerCase(name.charAt(0)) + name.substring(1);
        }

        String nullMessage() {
            return fieldName() + " must not be null";
        }

        @Override
        public String toString() {
            return type.getSimpleName();
        }
    }
}
