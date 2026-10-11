package com.springboot.project.businessrule.fact;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import tools.jackson.databind.json.JsonMapper;

class GenericObjectTest {

    private static final String JSON =
            """
            {
              "name": "Laptop",
              "price": 150,
              "ratio": 1.5,
              "inStock": true,
              "nothing": null,
              "order": {"total": 160, "createdAt": "2026-10-11T08:30:00", "items": null},
              "user": {"birthDate": "1992-09-18", "badDate": "18/09/1992"},
              "tags": ["a", 1, "b"],
              "counts": [1, "x", 3]
            }
            """;

    private final GenericObject genericObject =
            new GenericObject(JsonMapper.builder().build().readTree(JSON));

    @Test
    void should_read_typed_values_when_paths_exist() {
        assertThat(genericObject.getJsonPropertyAsString("/name")).isEqualTo("Laptop");
        assertThat(genericObject.getJsonPropertyAsInt("/order/total")).isEqualTo(160);
        assertThat(genericObject.getJsonPropertyAsLong("/price")).isEqualTo(150L);
        assertThat(genericObject.getJsonPropertyAsDouble("/ratio")).isEqualTo(1.5);
        assertThat(genericObject.getJsonPropertyAsFloat("/ratio")).isEqualTo(1.5f);
        assertThat(genericObject.getJsonPropertyAsBigDecimal("/ratio")).isEqualByComparingTo("1.5");
        assertThat(genericObject.getJsonPropertyAsBoolean("/inStock")).isTrue();
        assertThat(genericObject.getJsonPropertyAsLocalDate("/user/birthDate"))
                .isEqualTo(LocalDate.of(1992, 9, 18));
        assertThat(genericObject.getJsonPropertyAsLocalDateTime("/order/createdAt"))
                .isEqualTo(LocalDateTime.of(2026, 10, 11, 8, 30));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/missing", "/order/items", "/nothing", "/name"})
    void should_return_null_when_value_is_missing_null_or_not_numeric(String path) {
        assertThat(genericObject.getJsonPropertyAsInt(path)).isNull();
        assertThat(genericObject.getJsonPropertyAsBigDecimal(path)).isNull();
    }

    @Test
    void should_return_null_when_value_has_another_type() {
        assertThat(genericObject.getJsonPropertyAsString("/price")).isNull();
        assertThat(genericObject.getJsonPropertyAsBoolean("/name")).isNull();
    }

    @Test
    void should_return_null_when_date_is_not_iso_formatted() {
        assertThat(genericObject.getJsonPropertyAsLocalDate("/user/badDate")).isNull();
        assertThat(genericObject.getJsonPropertyAsLocalDateTime("/user/birthDate")).isNull();
    }

    @Test
    void should_keep_only_matching_elements_when_reading_arrays() {
        assertThat(genericObject.getJsonPropertyAsStringList("/tags")).containsExactly("a", "b");
        assertThat(genericObject.getJsonPropertyAsIntList("/counts")).containsExactly(1, 3);
    }

    @Test
    void should_return_null_when_reading_non_array_as_list() {
        assertThat(genericObject.getJsonPropertyAsStringList("/name")).isNull();
        assertThat(genericObject.getJsonPropertyAsIntList("/missing")).isNull();
    }

    @Test
    void should_return_missing_node_when_path_does_not_exist() {
        assertThat(genericObject.getJsonProperty("/missing").isMissingNode()).isTrue();
    }
}
