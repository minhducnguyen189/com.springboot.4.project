package com.springboot.project.businessrule.fact;

import lombok.Getter;
import lombok.Setter;
import tools.jackson.databind.node.ObjectNode;

/** Global exposed to rules as {@code result}; rules record their outcome into {@link #result}. */
@Getter
@Setter
public class ResultObject {

    private ObjectNode result;
}
