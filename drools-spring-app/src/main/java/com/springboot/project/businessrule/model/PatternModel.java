package com.springboot.project.businessrule.model;

import java.util.List;

public record PatternModel(String identifier, String type, List<String> conditions) {}
