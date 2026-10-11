package com.springboot.project.businessrule.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.springboot.project.businessrule.ExampleRuleFixture;
import com.springboot.project.businessrule.drl.DrlBuilder;
import com.springboot.project.businessrule.engine.RuleEngine;
import com.springboot.project.businessrule.entity.BusinessRuleEntity;
import com.springboot.project.businessrule.model.BusinessRuleDefinitionModel;
import com.springboot.project.businessrule.repository.BusinessRuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.node.ObjectNode;

@ExtendWith(MockitoExtension.class)
class BusinessRuleServiceTest {

    @Mock private BusinessRuleRepository businessRuleRepository;

    private BusinessRuleService businessRuleService;
    private ObjectNode exampleData;
    private BusinessRuleDefinitionModel exampleDefinition;

    @BeforeEach
    void setUp() {
        businessRuleService =
                new BusinessRuleService(businessRuleRepository, new DrlBuilder(), new RuleEngine());
        exampleData = ExampleRuleFixture.data();
        exampleDefinition = ExampleRuleFixture.definition();
    }

    @Test
    void should_record_fired_rule_outcomes_when_testing_example_rule() {
        // when
        ObjectNode result = businessRuleService.testBusinessRule(exampleData, exampleDefinition);

        // then
        assertThat(result.get("eligibleForDiscount").asBoolean()).isTrue();
        assertThat(result.get("conflict").asString()).isEqualTo("Missing order data");
        assertThat(result.get("youngUserBonus").asBoolean()).isFalse();
        assertThat(result.has("highValueOrder")).isFalse();
    }

    @Test
    void should_keep_input_data_untouched_when_testing_rule() {
        // given
        ObjectNode originalData = exampleData.deepCopy();

        // when
        ObjectNode result = businessRuleService.testBusinessRule(exampleData, exampleDefinition);

        // then
        assertThat(exampleData).isEqualTo(originalData);
        assertThat(result.get("name").asString()).isEqualTo("Laptop");
    }

    @Test
    void should_save_rule_named_after_definition_when_creating_rule() {
        // given
        when(businessRuleRepository.save(any(BusinessRuleEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        BusinessRuleEntity created = businessRuleService.createBusinessRule(exampleDefinition);

        // then
        ArgumentCaptor<BusinessRuleEntity> saved = ArgumentCaptor.forClass(BusinessRuleEntity.class);
        verify(businessRuleRepository).save(saved.capture());
        assertThat(saved.getValue().getId()).isNotNull();
        assertThat(saved.getValue().getName()).isEqualTo(ExampleRuleFixture.EXAMPLE_PACKAGE_NAME);
        assertThat(saved.getValue().getDefinition()).isEqualTo(exampleDefinition);
        assertThat(created).isSameAs(saved.getValue());
    }
}
