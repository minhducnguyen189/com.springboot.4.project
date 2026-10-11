package com.springboot.project.businessrule.model;

import java.util.List;

/** A DRL package: imports, globals and the rules it contains. */
public record BusinessRuleDefinitionModel(
        String name, List<String> imports, List<GlobalModel> globals, List<RuleModel> rules) {}
