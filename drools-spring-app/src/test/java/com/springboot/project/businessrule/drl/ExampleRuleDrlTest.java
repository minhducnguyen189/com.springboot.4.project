package com.springboot.project.businessrule.drl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.springboot.project.businessrule.ExampleRuleFixture;
import com.springboot.project.businessrule.engine.RuleEngine;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class ExampleRuleDrlTest {

    private static final Pattern RULE_DECLARATION = Pattern.compile("(?m)^rule \"([^\"]+)\"");

    private final String drl = new DrlBuilder().build(ExampleRuleFixture.definition());

    @Test
    void should_render_every_rule_and_conflict_when_building_example_drl() {
        // when
        Matcher ruleNames = RULE_DECLARATION.matcher(drl);

        // then
        assertThat(ruleNames.results().map(match -> match.group(1)))
                .containsExactly(
                        "HighValueOrderRule",
                        "MissingOrderFields",
                        "PremiumMemberDiscountRule",
                        "YoungUserSpecialRule",
                        "NotUserSpecialRule");
    }

    @Test
    void should_render_package_imports_and_global_when_building_example_drl() {
        // then
        assertThat(drl)
                .contains("package " + ExampleRuleFixture.EXAMPLE_PACKAGE_NAME)
                .contains("import com.springboot.project.common.util.DateTimeHelper")
                .contains("import com.springboot.project.businessrule.fact.GenericObject")
                .contains("global com.springboot.project.businessrule.fact.ResultObject result");
    }

    @Test
    void should_compile_when_example_drl_is_loaded_into_rule_engine() {
        // when / then
        assertThatCode(
                        () ->
                                new RuleEngine()
                                        .newSession(drl, ExampleRuleFixture.EXAMPLE_PACKAGE_NAME))
                .doesNotThrowAnyException();
    }
}
