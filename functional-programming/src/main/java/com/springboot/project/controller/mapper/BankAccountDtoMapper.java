package com.springboot.project.controller.mapper;

import com.springboot.project.generated.dto.BankAccountFilterRequestDto;
import com.springboot.project.generated.dto.BankAccountFilterResponseDto;
import com.springboot.project.service.bank_account.model.BankAccountFilterRequestModel;
import com.springboot.project.service.bank_account.model.BankAccountFilterResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BankAccountDtoMapper {

  BankAccountDtoMapper MAPPER = Mappers.getMapper(BankAccountDtoMapper.class);

  BankAccountFilterRequestModel toBankAccountFilterRequestModel(BankAccountFilterRequestDto requestDto);

  BankAccountFilterResponseDto toBankAccountFilterResponseDto(BankAccountFilterResponseModel responseModel);


}
