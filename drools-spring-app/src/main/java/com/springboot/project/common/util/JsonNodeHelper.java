package com.springboot.project.common.util;

import lombok.experimental.UtilityClass;
import tools.jackson.databind.JsonNode;

@UtilityClass
public class JsonNodeHelper {

    public static JsonNode getChild(JsonNode parent, String childName) {
        return parent.get(childName);
    }
}
