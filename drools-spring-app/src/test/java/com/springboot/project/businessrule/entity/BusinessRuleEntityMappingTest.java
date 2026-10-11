package com.springboot.project.businessrule.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.springboot.project.businessrule.model.BusinessRuleDefinitionModel;
import com.springboot.project.businessrule.model.ConflictModel;
import com.springboot.project.businessrule.model.ConsequenceModel;
import com.springboot.project.businessrule.model.GlobalModel;
import com.springboot.project.businessrule.model.PatternModel;
import com.springboot.project.businessrule.model.RuleModel;
import java.util.List;
import java.util.UUID;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.NoOpDbRefResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

class BusinessRuleEntityMappingTest {

    private final MappingMongoConverter converter =
            new MappingMongoConverter(NoOpDbRefResolver.INSTANCE, new MongoMappingContext());

    @Test
    void should_read_back_same_definition_when_entity_is_written_to_document() {
        // given
        BusinessRuleDefinitionModel definition =
                new BusinessRuleDefinitionModel(
                        "OrderRules",
                        List.of("com.example.GenericObject"),
                        List.of(new GlobalModel("com.example.ResultObject", "result")),
                        List.of(
                                new RuleModel(
                                        "HighValueOrderRule",
                                        List.of(new PatternModel("$data", "GenericObject", List.of("a > 1"))),
                                        List.of(new ConsequenceModel(List.of("act()"))),
                                        List.of(
                                                new ConflictModel(
                                                        "Missing",
                                                        "$data",
                                                        "GenericObject",
                                                        List.of("a == null"),
                                                        List.of("flag()"),
                                                        "a is missing")))));
        BusinessRuleEntity entity = new BusinessRuleEntity(UUID.randomUUID(), "OrderRules", definition);
        Document document = new Document();

        // when
        converter.write(entity, document);
        BusinessRuleEntity readBack = converter.read(BusinessRuleEntity.class, document);

        // then
        assertThat(readBack.getId()).isEqualTo(entity.getId());
        assertThat(readBack.getName()).isEqualTo("OrderRules");
        assertThat(readBack.getDefinition()).isEqualTo(definition);
    }
}
