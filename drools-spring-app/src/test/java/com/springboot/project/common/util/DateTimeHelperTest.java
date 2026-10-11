package com.springboot.project.common.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DateTimeHelperTest {

    @Test
    void should_parse_iso_date_when_no_format_is_given() {
        assertThat(DateTimeHelper.toLocalDate("1992-09-18")).isEqualTo(LocalDate.of(1992, 9, 18));
    }

    @Test
    void should_parse_date_with_given_format() {
        assertThat(DateTimeHelper.toLocalDate("18/09/1992", "dd/MM/yyyy"))
                .isEqualTo(LocalDate.of(1992, 9, 18));
    }

    @Test
    void should_throw_when_date_does_not_match_default_format() {
        assertThatThrownBy(() -> DateTimeHelper.toLocalDate("18/09/1992"))
                .isInstanceOf(DateTimeParseException.class);
    }

    @ParameterizedTest(name = "{0} at {1} -> {2}")
    @CsvSource({
        "1992-09-18, 2026-10-11, 34",
        "1996-10-11, 2026-10-11, 30",
        "1996-10-12, 2026-10-11, 29",
        "2026-10-11, 2026-10-11, 0"
    })
    void should_count_completed_years_when_calculating_age(
            LocalDate birthDate, LocalDate currentDate, int expectedAge) {
        assertThat(DateTimeHelper.calculateAge(birthDate, currentDate)).isEqualTo(expectedAge);
    }

    @Test
    void should_return_zero_when_a_date_is_missing() {
        assertThat(DateTimeHelper.calculateAge(null, LocalDate.of(2026, 10, 11))).isZero();
        assertThat(DateTimeHelper.calculateAge(LocalDate.of(1992, 9, 18), null)).isZero();
    }
}
