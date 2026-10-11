package com.springboot.project.businessrule.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.springboot.project.businessrule.ExampleRuleFixture;
import com.springboot.project.businessrule.entity.BusinessRuleEntity;
import com.springboot.project.businessrule.generated.dto.BusinessRuleResponseDto;
import com.springboot.project.businessrule.model.BusinessRuleDefinitionModel;
import com.springboot.project.businessrule.model.ConflictModel;
import com.springboot.project.businessrule.model.GlobalModel;
import com.springboot.project.businessrule.model.RuleModel;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class BusinessRuleDtoMapperTest {

    @Test
    void should_map_example_definition_dto_to_model() {
        // when
        BusinessRuleDefinitionModel model = ExampleRuleFixture.definition();

        // then
        assertThat(model.name()).isEqualTo(ExampleRuleFixture.EXAMPLE_PACKAGE_NAME);
        assertThat(model.imports()).hasSize(4).contains("java.time.LocalDate");
        assertThat(model.globals())
                .containsExactly(
                        new GlobalModel(
                                "com.springboot.project.businessrule.fact.ResultObject", "result"));
        assertThat(model.rules()).extracting(RuleModel::rule).hasSize(4);
        RuleModel highValueOrder = model.rules().getFirst();
        assertThat(highValueOrder.when().getFirst().conditions()).hasSize(4);
        assertThat(highValueOrder.then().getFirst().actions()).hasSize(2);
        assertThat(highValueOrder.conflicts())
                .singleElement()
                .extracting(ConflictModel::name, ConflictModel::conflictDescription)
                .containsExactly("MissingOrderFields", "Some required fields are missing");
    }

    @Test
    void should_map_entity_to_response_dto_with_definition() {
        // given
        BusinessRuleEntity entity =
                new BusinessRuleEntity(
                        UUID.randomUUID(),
                        ExampleRuleFixture.EXAMPLE_PACKAGE_NAME,
                        ExampleRuleFixture.definition());

        // when
        BusinessRuleResponseDto response = BusinessRuleDtoMapper.MAPPER.toResponseDto(entity);

        // then
        assertThat(response.getId()).isEqualTo(entity.getId());
        assertThat(response.getName()).isEqualTo(entity.getName());
        assertThat(response.getDefinition()).isEqualTo(ExampleRuleFixture.definitionDto());
    }
}
