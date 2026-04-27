package com.springboot.project.controller.mapper;

import com.springboot.project.generated.dto.*;
import com.springboot.project.service.bank_account.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BankAccountDtoMapper {

  BankAccountDtoMapper MAPPER = Mappers.getMapper(BankAccountDtoMapper.class);

  BankAccountFilterRequestModel toBankAccountFilterRequestModel(BankAccountFilterRequestDto requestDto);

  BankAccountFilterResponseDto toBankAccountFilterResponseDto(BankAccountFilterResponseModel responseModel);

  BankAccountDetailDto toBankAccountDetailDto(BankAccountDetailModel detailModel);

  CreateBankAccountRequestModel toCreateBankAccountRequestModel(CreateBankAccountRequestDto requestDto);

  UpdateBankAccountRequestModel toUpdateBankAccountRequestModel(UpdateBankAccountRequestDto requestDto);
}
