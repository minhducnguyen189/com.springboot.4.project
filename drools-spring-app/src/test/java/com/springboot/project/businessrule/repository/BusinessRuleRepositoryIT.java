package com.springboot.project.businessrule.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.springboot.project.MongoContainerConfiguration;
import com.springboot.project.businessrule.ExampleRuleFixture;
import com.springboot.project.businessrule.entity.BusinessRuleEntity;
import java.util.UUID;
import org.bson.BsonBinarySubType;
import org.bson.BsonDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataMongoTest
@Import(MongoContainerConfiguration.class)
@Testcontainers(disabledWithoutDocker = true)
class BusinessRuleRepositoryIT {

    private static final String COLLECTION = "business_rules";

    @Autowired private BusinessRuleRepository businessRuleRepository;
    @Autowired private MongoTemplate mongoTemplate;

    @BeforeEach
    void cleanCollection() {
        businessRuleRepository.deleteAll();
    }

    @Test
    void should_read_back_example_definition_when_entity_is_saved() {
        // given
        BusinessRuleEntity entity = exampleEntity();

        // when
        businessRuleRepository.save(entity);

        // then
        assertThat(businessRuleRepository.findById(entity.getId()))
                .get()
                .satisfies(
                        found -> {
                            assertThat(found.getName())
                                    .isEqualTo(ExampleRuleFixture.EXAMPLE_PACKAGE_NAME);
                            assertThat(found.getDefinition())
                                    .isEqualTo(ExampleRuleFixture.definition());
                        });
    }

    @Test
    void should_return_all_saved_rules_when_finding_all() {
        // given
        businessRuleRepository.save(exampleEntity());
        businessRuleRepository.save(exampleEntity());

        // when / then
        assertThat(businessRuleRepository.findAll()).hasSize(2);
    }

    @Test
    void should_store_id_as_standard_uuid_binary_when_entity_is_saved() {
        // given
        businessRuleRepository.save(exampleEntity());

        // when
        BsonDocument stored =
                mongoTemplate
                        .getCollection(COLLECTION)
                        .withDocumentClass(BsonDocument.class)
                        .find()
                        .first();

        // then
        assertThat(stored).isNotNull();
        assertThat(stored.getBinary("_id").getType())
                .isEqualTo(BsonBinarySubType.UUID_STANDARD.getValue());
        assertThat(stored.getDocument("definition").getArray("rules")).hasSize(4);
    }

    private static BusinessRuleEntity exampleEntity() {
        return new BusinessRuleEntity(
                UUID.randomUUID(),
                ExampleRuleFixture.EXAMPLE_PACKAGE_NAME,
                ExampleRuleFixture.definition());
    }
}
