package com.springboot.project.businessrule.model;

import java.util.List;

/** An extra rule fired when the facts are inconsistent with what the owning rule expects. */
public record ConflictModel(
        String name,
        String identifier,
        String type,
        List<String> conditions,
        List<String> actions,
        String conflictDescription) {}
