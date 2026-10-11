package com.springboot.project.businessrule.model;

import java.util.List;

public record RuleModel(
        String rule,
        List<PatternModel> when,
        List<ConsequenceModel> then,
        List<ConflictModel> conflicts) {}
