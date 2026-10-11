package com.springboot.project.businessrule.service;

import com.springboot.project.businessrule.entity.BusinessRuleEntity;
import com.springboot.project.businessrule.model.BusinessRuleDefinitionModel;
import java.util.List;
import tools.jackson.databind.node.ObjectNode;

public interface IBusinessRuleService {

    BusinessRuleEntity createBusinessRule(BusinessRuleDefinitionModel definition);

    List<BusinessRuleEntity> getBusinessRules();

    /**
     * Compiles the definition and fires its rules against {@code data}.
     *
     * @return a copy of {@code data} updated by the fired rules
     */
    ObjectNode testBusinessRule(ObjectNode data, BusinessRuleDefinitionModel definition);
}
