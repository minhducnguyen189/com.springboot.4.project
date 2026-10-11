package com.springboot.project.businessrule.service;

import com.springboot.project.businessrule.drl.DrlBuilder;
import com.springboot.project.businessrule.engine.RuleEngine;
import com.springboot.project.businessrule.entity.BusinessRuleEntity;
import com.springboot.project.businessrule.fact.GenericObject;
import com.springboot.project.businessrule.fact.ResultObject;
import com.springboot.project.businessrule.model.BusinessRuleDefinitionModel;
import com.springboot.project.businessrule.repository.BusinessRuleRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.StatelessKieSession;
import org.springframework.stereotype.Service;
import tools.jackson.databind.node.ObjectNode;

@Service
@RequiredArgsConstructor
public class BusinessRuleService implements IBusinessRuleService {

    /** Name under which rules reference the {@link ResultObject} global. */
    private static final String RESULT_GLOBAL = "result";

    private final BusinessRuleRepository businessRuleRepository;
    private final DrlBuilder drlBuilder;
    private final RuleEngine ruleEngine;

    @Override
    public BusinessRuleEntity createBusinessRule(BusinessRuleDefinitionModel definition) {
        BusinessRuleEntity businessRule =
                new BusinessRuleEntity(UUID.randomUUID(), definition.name(), definition);
        return businessRuleRepository.save(businessRule);
    }

    @Override
    public List<BusinessRuleEntity> getBusinessRules() {
        return businessRuleRepository.findAll();
    }

    @Override
    public ObjectNode testBusinessRule(ObjectNode data, BusinessRuleDefinitionModel definition) {
        String drl = drlBuilder.build(definition);
        StatelessKieSession session = ruleEngine.newSession(drl, definition.name());
        ResultObject resultObject = new ResultObject();
        resultObject.setResult(data.deepCopy());
        session.setGlobal(RESULT_GLOBAL, resultObject);
        session.execute(List.of(new GenericObject(data), resultObject));
        return resultObject.getResult();
    }
}
