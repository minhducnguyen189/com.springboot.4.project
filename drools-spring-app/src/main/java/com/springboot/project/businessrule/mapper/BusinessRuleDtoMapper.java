package com.springboot.project.businessrule.mapper;

import com.springboot.project.businessrule.entity.BusinessRuleEntity;
import com.springboot.project.businessrule.generated.dto.BusinessRuleDefinitionDto;
import com.springboot.project.businessrule.generated.dto.BusinessRuleResponseDto;
import com.springboot.project.businessrule.model.BusinessRuleDefinitionModel;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BusinessRuleDtoMapper {

    BusinessRuleDtoMapper MAPPER = Mappers.getMapper(BusinessRuleDtoMapper.class);

    BusinessRuleDefinitionModel toModel(BusinessRuleDefinitionDto definitionDto);

    BusinessRuleResponseDto toResponseDto(BusinessRuleEntity entity);

    List<BusinessRuleResponseDto> toResponseDtos(List<BusinessRuleEntity> entities);
}
