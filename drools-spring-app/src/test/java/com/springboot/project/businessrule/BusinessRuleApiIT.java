package com.springboot.project.businessrule;

import static org.assertj.core.api.Assertions.assertThat;

import com.springboot.project.MongoContainerConfiguration;
import com.springboot.project.businessrule.repository.BusinessRuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

/** Full application on a random port against a MongoDB Testcontainer. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@Import(MongoContainerConfiguration.class)
@Testcontainers(disabledWithoutDocker = true)
class BusinessRuleApiIT {

    private static final String BUSINESS_RULES_PATH = "/v1/drools/business-rules";
    private static final String TEST_RULE_PATH = "/v1/drools/business-rules/actions/test";

    @Autowired private RestTestClient restTestClient;
    @Autowired private BusinessRuleRepository businessRuleRepository;
    @Autowired private JsonMapper jsonMapper;

    @BeforeEach
    void cleanCollection() {
        businessRuleRepository.deleteAll();
    }

    @Test
    void should_store_example_rule_and_list_it_when_created() {
        // when
        restTestClient
                .post()
                .uri(BUSINESS_RULES_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createRequestBody())
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .jsonPath("$.id")
                .isNotEmpty()
                .jsonPath("$.name")
                .isEqualTo(ExampleRuleFixture.EXAMPLE_PACKAGE_NAME)
                .jsonPath("$.definition.rules.length()")
                .isEqualTo(4);

        // then
        restTestClient
                .get()
                .uri(BUSINESS_RULES_PATH)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.length()")
                .isEqualTo(1)
                .jsonPath("$[0].definition.rules[0].conflicts[0].name")
                .isEqualTo("MissingOrderFields");
    }

    @Test
    void should_return_rule_outcomes_when_testing_example_rule() {
        restTestClient
                .post()
                .uri(TEST_RULE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonMapper.writeValueAsString(ExampleRuleFixture.request()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.result.eligibleForDiscount")
                .isEqualTo(true)
                .jsonPath("$.result.conflict")
                .isEqualTo("Missing order data")
                .jsonPath("$.result.youngUserBonus")
                .isEqualTo(false)
                .jsonPath("$.result.highValueOrder")
                .doesNotExist()
                .jsonPath("$.result.name")
                .isEqualTo("Laptop");
    }

    @Test
    void should_respond_400_with_drools_message_when_rule_does_not_compile() {
        // given
        ObjectNode request = ExampleRuleFixture.request();
        ObjectNode firstPattern = (ObjectNode) request.at("/businessRule/rules/0/when/0");
        firstPattern.put("type", "UnknownFact");

        // when / then
        restTestClient
                .post()
                .uri(TEST_RULE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonMapper.writeValueAsString(request))
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody()
                .jsonPath("$.code")
                .isEqualTo(400)
                .jsonPath("$.message")
                .value(
                        String.class,
                        message -> assertThat(message).contains("UnknownFact"));
    }

    @Test
    void should_respond_400_and_store_nothing_when_definition_has_no_rules() {
        // given
        ObjectNode definition = ExampleRuleFixture.definitionJson();
        definition.remove("rules");

        // when
        restTestClient
                .post()
                .uri(BUSINESS_RULES_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonMapper.writeValueAsString(wrapAsCreateRequest(definition)))
                .exchange()
                .expectStatus()
                .isBadRequest();

        // then
        assertThat(businessRuleRepository.count()).isZero();
    }

    private String createRequestBody() {
        return jsonMapper.writeValueAsString(
                wrapAsCreateRequest(ExampleRuleFixture.definitionJson()));
    }

    private ObjectNode wrapAsCreateRequest(ObjectNode definition) {
        ObjectNode request = jsonMapper.createObjectNode();
        request.set("businessRule", definition);
        return request;
    }
}
