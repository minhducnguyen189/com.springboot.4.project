package com.springboot.project.businessrule.drl;

import static org.assertj.core.api.Assertions.assertThat;

import com.springboot.project.businessrule.model.BusinessRuleDefinitionModel;
import com.springboot.project.businessrule.model.ConflictModel;
import com.springboot.project.businessrule.model.ConsequenceModel;
import com.springboot.project.businessrule.model.GlobalModel;
import com.springboot.project.businessrule.model.PatternModel;
import com.springboot.project.businessrule.model.RuleModel;
import java.util.List;
import org.junit.jupiter.api.Test;

class DrlBuilderTest {

    private final DrlBuilder drlBuilder = new DrlBuilder();

    @Test
    void should_render_package_imports_globals_and_rules_when_building_drl() {
        // given
        BusinessRuleDefinitionModel definition =
                definitionWithRule(
                        new RuleModel(
                                "HighValueOrderRule",
                                List.of(
                                        new PatternModel(
                                                "$data",
                                                "GenericObject",
                                                List.of("$total: getJsonPropertyAsInt(\"/total\") != null", "$total > 100"))),
                                List.of(new ConsequenceModel(List.of("first()", "second()"))),
                                List.of()));

        // when
        String drl = drlBuilder.build(definition);

        // then
        assertThat(drl)
                .contains("package OrderRules")
                .contains("import com.example.GenericObject")
                .contains("global com.example.ResultObject result")
                .contains("rule \"HighValueOrderRule\"")
                .contains("$data : GenericObject(")
                .contains("$total: getJsonPropertyAsInt(\"/total\") != null")
                .contains("$total > 100")
                .contains("first();second();");
    }

    @Test
    void should_use_conflict_own_pattern_when_building_conflict_rule() {
        // given
        ConflictModel conflict =
                new ConflictModel(
                        "MissingTotal",
                        "$order",
                        "OrderFact",
                        List.of("total == null"),
                        List.of("flagConflict()"),
                        "total is missing");
        BusinessRuleDefinitionModel definition =
                definitionWithRule(
                        new RuleModel(
                                "HighValueOrderRule",
                                List.of(new PatternModel("$data", "GenericObject", List.of())),
                                List.of(new ConsequenceModel(List.of("noop()"))),
                                List.of(conflict)));

        // when
        String drl = drlBuilder.build(definition);

        // then
        assertThat(drl)
                .contains("rule \"MissingTotal\"")
                .contains("$order : OrderFact(")
                .contains("total == null")
                .contains("flagConflict();");
    }

    @Test
    void should_skip_conflicts_and_optional_sections_when_they_are_null() {
        // given
        BusinessRuleDefinitionModel definition =
                new BusinessRuleDefinitionModel(
                        "OrderRules",
                        null,
                        null,
                        List.of(
                                new RuleModel(
                                        "OnlyRule",
                                        List.of(new PatternModel("$data", "GenericObject", List.of())),
                                        List.of(new ConsequenceModel(List.of("noop()"))),
                                        null)));

        // when
        String drl = drlBuilder.build(definition);

        // then
        assertThat(drl).contains("rule \"OnlyRule\"").doesNotContain("import").doesNotContain("global");
    }

    private static BusinessRuleDefinitionModel definitionWithRule(RuleModel rule) {
        return new BusinessRuleDefinitionModel(
                "OrderRules",
                List.of("com.example.GenericObject"),
                List.of(new GlobalModel("com.example.ResultObject", "result")),
                List.of(rule));
    }
}
