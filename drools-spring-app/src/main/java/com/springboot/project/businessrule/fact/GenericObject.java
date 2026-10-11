package com.springboot.project.businessrule.fact;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import tools.jackson.databind.JsonNode;

public class GenericObject {

    private final JsonNode rootNode;

    public GenericObject(JsonNode rootNode) {
        this.rootNode = rootNode;
    }

    /** Get a JsonNode using JSON Pointer syntax (e.g. /order/price). */
    public JsonNode getJsonProperty(String pointerPath) {
        return rootNode.at(pointerPath);
    }

    /** Get a property as a String. Returns null if path is missing or not textual. */
    public String getJsonPropertyAsString(String pointerPath) {
        JsonNode node = getJsonProperty(pointerPath);
        return node.isMissingNode() || !node.isString() ? null : node.asString();
    }

    /** Get a property as an Integer. Returns null if path is missing or not numeric. */
    public Integer getJsonPropertyAsInt(String pointerPath) {
        JsonNode node = getJsonProperty(pointerPath);
        return node.isMissingNode() || !node.isNumber() ? null : node.asInt();
    }

    /** Get a property as a Boolean. Returns null if path is missing or not a boolean. */
    public Boolean getJsonPropertyAsBoolean(String pointerPath) {
        JsonNode node = getJsonProperty(pointerPath);
        return node.isMissingNode() || !node.isBoolean() ? null : node.asBoolean();
    }

    /** Get a property as a Long. Returns null if path is missing or not a numeric. */
    public Long getJsonPropertyAsLong(String pointerPath) {
        JsonNode node = getJsonProperty(pointerPath);
        return node.isMissingNode() || !node.isNumber() ? null : node.asLong();
    }

    /** Get a property as a Float. Returns null if path is missing or not a numeric. */
    public Float getJsonPropertyAsFloat(String pointerPath) {
        JsonNode node = getJsonProperty(pointerPath);
        return node.isMissingNode() || !node.isNumber() ? null : node.floatValue();
    }

    /** Get a property as a Double. Returns null if path is missing or not numeric. */
    public Double getJsonPropertyAsDouble(String pointerPath) {
        JsonNode node = getJsonProperty(pointerPath);
        return node.isMissingNode() || !node.isNumber() ? null : node.asDouble();
    }

    /** Get a property as a BigDecimal. Returns null if path is missing or not numeric. */
    public BigDecimal getJsonPropertyAsBigDecimal(String pointerPath) {
        JsonNode node = getJsonProperty(pointerPath);
        return node.isMissingNode() || !node.isNumber() ? null : node.decimalValue();
    }

    /** Get a property as a LocalDate. Returns null if path is missing or unparsable. */
    public LocalDate getJsonPropertyAsLocalDate(String pointerPath) {
        String value = getJsonPropertyAsString(pointerPath);
        if (value == null) return null;
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /** Get a property as a LocalDateTime. Returns null if path is missing or unparsable. */
    public LocalDateTime getJsonPropertyAsLocalDateTime(String pointerPath) {
        String value = getJsonPropertyAsString(pointerPath);
        if (value == null) return null;
        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /** Get a JSON array as a List<String>. Returns null if node is not an array. */
    public List<String> getJsonPropertyAsStringList(String pointerPath) {
        JsonNode node = getJsonProperty(pointerPath);
        if (!node.isArray()) return null;
        List<String> result = new ArrayList<>();
        for (JsonNode item : node) {
            if (item.isString()) {
                result.add(item.asString());
            }
        }
        return result;
    }

    /** Get a JSON array as a List<Integer>. Returns null if node is not an array. */
    public List<Integer> getJsonPropertyAsIntList(String pointerPath) {
        JsonNode node = getJsonProperty(pointerPath);
        if (!node.isArray()) return null;
        List<Integer> result = new ArrayList<>();
        for (JsonNode item : node) {
            if (item.isInt()) {
                result.add(item.asInt());
            }
        }
        return result;
    }
}
