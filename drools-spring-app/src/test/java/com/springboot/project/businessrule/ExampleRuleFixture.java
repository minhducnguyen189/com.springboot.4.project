package com.springboot.project.businessrule;

import com.springboot.project.businessrule.generated.dto.BusinessRuleDefinitionDto;
import com.springboot.project.businessrule.mapper.BusinessRuleDtoMapper;
import com.springboot.project.businessrule.model.BusinessRuleDefinitionModel;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

/** Loads {@code example/complex-rule-with-utility-classes.json}; every call returns fresh copies. */
public final class ExampleRuleFixture {

    public static final String EXAMPLE_RULE_RESOURCE =
            "/example/complex-rule-with-utility-classes.json";
    public static final String EXAMPLE_PACKAGE_NAME = "ComplexBusinessRulePackage";

    private static final JsonMapper JSON_MAPPER = JsonMapper.builder().build();
    private static final ObjectNode EXAMPLE_REQUEST = load();

    private ExampleRuleFixture() {}

    /** The whole example request body: {@code {data, businessRule}}. */
    public static ObjectNode request() {
        return EXAMPLE_REQUEST.deepCopy();
    }

    public static ObjectNode data() {
        return (ObjectNode) request().get("data");
    }

    public static ObjectNode definitionJson() {
        return (ObjectNode) request().get("businessRule");
    }

    public static BusinessRuleDefinitionDto definitionDto() {
        return JSON_MAPPER.treeToValue(definitionJson(), BusinessRuleDefinitionDto.class);
    }

    public static BusinessRuleDefinitionModel definition() {
        return BusinessRuleDtoMapper.MAPPER.toModel(definitionDto());
    }

    private static ObjectNode load() {
        try (InputStream example = ExampleRuleFixture.class.getResourceAsStream(EXAMPLE_RULE_RESOURCE)) {
            JsonNode request = JSON_MAPPER.readTree(example);
            return (ObjectNode) request;
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read " + EXAMPLE_RULE_RESOURCE, e);
        }
    }
}
