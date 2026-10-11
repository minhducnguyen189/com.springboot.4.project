package com.springboot.project.businessrule.controller;

import com.springboot.project.businessrule.entity.BusinessRuleEntity;
import com.springboot.project.businessrule.generated.api.BusinessRuleApi;
import com.springboot.project.businessrule.generated.dto.BusinessRuleResponseDto;
import com.springboot.project.businessrule.generated.dto.CreateBusinessRuleRequestDto;
import com.springboot.project.businessrule.generated.dto.TestBusinessRuleRequestDto;
import com.springboot.project.businessrule.generated.dto.TestBusinessRuleResponseDto;
import com.springboot.project.businessrule.mapper.BusinessRuleDtoMapper;
import com.springboot.project.businessrule.model.BusinessRuleDefinitionModel;
import com.springboot.project.businessrule.service.IBusinessRuleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

@RestController
@RequiredArgsConstructor
public class BusinessRuleController implements BusinessRuleApi {

    private final IBusinessRuleService businessRuleService;
    private final JsonMapper jsonMapper;

    @Override
    public ResponseEntity<BusinessRuleResponseDto> createBusinessRule(
            CreateBusinessRuleRequestDto createBusinessRuleRequestDto) {
        BusinessRuleDefinitionModel definition =
                BusinessRuleDtoMapper.MAPPER.toModel(createBusinessRuleRequestDto.getBusinessRule());
        BusinessRuleEntity created = businessRuleService.createBusinessRule(definition);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BusinessRuleDtoMapper.MAPPER.toResponseDto(created));
    }

    @Override
    public ResponseEntity<List<BusinessRuleResponseDto>> getBusinessRules() {
        List<BusinessRuleEntity> businessRules = businessRuleService.getBusinessRules();
        return ResponseEntity.ok(BusinessRuleDtoMapper.MAPPER.toResponseDtos(businessRules));
    }

    @Override
    public ResponseEntity<TestBusinessRuleResponseDto> testBusinessRule(
            TestBusinessRuleRequestDto testBusinessRuleRequestDto) {
        ObjectNode data = jsonMapper.valueToTree(testBusinessRuleRequestDto.getData());
        BusinessRuleDefinitionModel definition =
                BusinessRuleDtoMapper.MAPPER.toModel(testBusinessRuleRequestDto.getBusinessRule());
        ObjectNode result = businessRuleService.testBusinessRule(data, definition);
        return ResponseEntity.ok(new TestBusinessRuleResponseDto().result(result));
    }
}
