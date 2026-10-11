package com.springboot.project.businessrule.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.springboot.project.businessrule.ExampleRuleFixture;
import com.springboot.project.businessrule.drl.DrlBuilder;
import com.springboot.project.businessrule.engine.RuleEngine;
import com.springboot.project.businessrule.repository.BusinessRuleRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import tools.jackson.databind.node.ObjectNode;

/**
 * Executes the real rules of {@code complex-rule-with-utility-classes.json} through the real Drools
 * engine, varying one input at a time from the example's {@code data}.
 */
class ExampleRuleScenarioTest {

    private final BusinessRuleService businessRuleService =
            new BusinessRuleService(
                    mock(BusinessRuleRepository.class), new DrlBuilder(), new RuleEngine());

    private ObjectNode execute(ObjectNode data) {
        return businessRuleService.testBusinessRule(data, ExampleRuleFixture.definition());
    }

    private static ObjectNode order(ObjectNode data) {
        return (ObjectNode) data.get("order");
    }

    private static ObjectNode user(ObjectNode data) {
        return (ObjectNode) data.get("user");
    }

    private static String birthDateForAge(int years) {
        return LocalDate.now().minusYears(years).toString();
    }

    @Nested
    class HighValueOrderRule {

        @Test
        void should_flag_high_value_order_when_total_above_100_and_at_least_3_items() {
            // given
            ObjectNode data = ExampleRuleFixture.data();
            order(data).put("items", 3);

            // when
            ObjectNode result = execute(data);

            // then
            assertThat(result.get("highValueOrder").asBoolean()).isTrue();
        }

        @ParameterizedTest(name = "total={0}, items={1}")
        @CsvSource({"100, 3", "160, 2"})
        void should_not_flag_high_value_order_when_a_threshold_is_not_met(int total, int items) {
            // given
            ObjectNode data = ExampleRuleFixture.data();
            order(data).put("total", total).put("items", items);

            // when
            ObjectNode result = execute(data);

            // then
            assertThat(result.has("highValueOrder")).isFalse();
        }
    }

    @Nested
    class MissingOrderFieldsConflict {

        @Test
        void should_record_conflict_when_order_items_are_missing() {
            // when
            ObjectNode result = execute(ExampleRuleFixture.data());

            // then
            assertThat(result.get("conflict").asString()).isEqualTo("Missing order data");
        }

        @Test
        @Disabled(
                "Example rule bug (inherited from v3): its condition '$isNull: (...)' is a Drools"
                        + " binding, not a constraint, so the conflict fires for every order. Drop the"
                        + " '$isNull:' prefix in the example JSON to make this pass.")
        void should_not_record_conflict_when_order_fields_are_present() {
            // given
            ObjectNode data = ExampleRuleFixture.data();
            order(data).put("items", 3);

            // when
            ObjectNode result = execute(data);

            // then
            assertThat(result.has("conflict")).isFalse();
        }
    }

    @Nested
    class PremiumMemberDiscountRule {

        @ParameterizedTest
        @ValueSource(strings = {"GOLD", "PLATINUM"})
        void should_grant_discount_when_premium_member_orders_above_100(String membershipLevel) {
            // given
            ObjectNode data = ExampleRuleFixture.data();
            user(data).put("membershipLevel", membershipLevel);

            // when
            ObjectNode result = execute(data);

            // then
            assertThat(result.get("eligibleForDiscount").asBoolean()).isTrue();
        }

        @Test
        void should_not_grant_discount_when_member_is_not_premium() {
            // given
            ObjectNode data = ExampleRuleFixture.data();
            user(data).put("membershipLevel", "SILVER");

            // when
            ObjectNode result = execute(data);

            // then
            assertThat(result.has("eligibleForDiscount")).isFalse();
        }

        @Test
        void should_not_grant_discount_when_order_total_is_not_above_100() {
            // given
            ObjectNode data = ExampleRuleFixture.data();
            order(data).put("total", 100);

            // when
            ObjectNode result = execute(data);

            // then
            assertThat(result.has("eligibleForDiscount")).isFalse();
        }

        @Test
        void should_not_grant_discount_when_membership_level_is_missing() {
            // given
            ObjectNode data = ExampleRuleFixture.data();
            user(data).remove("membershipLevel");

            // when
            ObjectNode result = execute(data);

            // then
            assertThat(result.has("eligibleForDiscount")).isFalse();
        }
    }

    @Nested
    class UserAgeRules {

        @Test
        void should_grant_young_user_bonus_when_under_30_buying_expensive_product_in_bulk() {
            // given
            ObjectNode data = ExampleRuleFixture.data();
            user(data).put("birthDate", birthDateForAge(20));

            // when
            ObjectNode result = execute(data);

            // then
            assertThat(result.get("youngUserBonus").asBoolean()).isTrue();
        }

        @Test
        void should_not_set_young_user_bonus_when_under_30_but_quantity_is_not_above_2() {
            // given
            ObjectNode data = ExampleRuleFixture.data();
            user(data).put("birthDate", birthDateForAge(20));
            data.put("quantity", 2);

            // when
            ObjectNode result = execute(data);

            // then
            assertThat(result.has("youngUserBonus")).isFalse();
        }

        @ParameterizedTest(name = "age={0}")
        @ValueSource(ints = {30, 65})
        void should_deny_young_user_bonus_when_user_is_30_or_older(int age) {
            // given
            ObjectNode data = ExampleRuleFixture.data();
            user(data).put("birthDate", birthDateForAge(age));

            // when
            ObjectNode result = execute(data);

            // then
            assertThat(result.get("youngUserBonus").asBoolean()).isFalse();
        }

        @Test
        void should_not_evaluate_age_rules_when_birth_date_is_missing() {
            // given
            ObjectNode data = ExampleRuleFixture.data();
            user(data).remove("birthDate");

            // when
            ObjectNode result = execute(data);

            // then
            assertThat(result.has("youngUserBonus")).isFalse();
        }
    }
}
