package com.springboot.project.businessrule.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.springboot.project.common.exception.BadRequestException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.StatelessKieSession;

class RuleEngineTest {

    private static final String PACKAGE_NAME = "TestRules";

    private final RuleEngine ruleEngine = new RuleEngine();

    @Test
    void should_throw_bad_request_when_drl_does_not_compile() {
        // given
        String invalidDrl =
                """
                package TestRules
                rule "Broken"
                when
                    UnknownType()
                then
                end
                """;

        // when / then
        assertThatThrownBy(() -> ruleEngine.newSession(invalidDrl, PACKAGE_NAME))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Business rule does not compile")
                .hasMessageContaining("UnknownType");
    }

    @Test
    void should_fire_rule_and_update_global_when_drl_is_valid() {
        // given
        String drl =
                """
                package TestRules
                global java.util.List fired
                rule "MatchString"
                when
                    $value : String(this == "hello")
                then
                    fired.add($value);
                end
                """;
        List<String> fired = new ArrayList<>();

        // when
        StatelessKieSession session = ruleEngine.newSession(drl, PACKAGE_NAME);
        session.setGlobal("fired", fired);
        session.execute(List.of("hello", "world"));

        // then
        assertThat(fired).containsExactly("hello");
    }
}
