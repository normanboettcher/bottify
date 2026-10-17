package de.bottify.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RatingTest {

    @ParameterizedTest
    @ValueSource(ints = {Rating.MIN, Rating.MAX})
    void creates_rating_at_valid_boundaries(int value) {
        // when
        var rating = new Rating(value);

        // then
        assertThat(rating.value()).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 6})
    void throws_on_value_outside_valid_range(int value) {
        // when/then
        assertThatThrownBy(() -> new Rating(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Rating value must be between [0] and [5]");
    }

    @Test
    void compares_ratings_by_value() {
        // given
        var lowerRating = new Rating(2);
        var equalRating = new Rating(2);
        var higherRating = new Rating(4);

        // then
        assertThat(lowerRating.compareTo(equalRating)).isZero();
        assertThat(lowerRating.compareTo(higherRating)).isNegative();
        assertThat(higherRating.compareTo(lowerRating)).isPositive();
    }
}
