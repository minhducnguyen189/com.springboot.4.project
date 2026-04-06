package com.springboot.project.controller.mapper;

import com.springboot.project.generated.dto.CreateTransactionRequestDto;
import com.springboot.project.generated.dto.TransactionDetailDto;
import com.springboot.project.generated.dto.TransactionFilterRequestDto;
import com.springboot.project.generated.dto.TransactionFilterResponseDto;
import com.springboot.project.generated.dto.UpdateTransactionRequestDto;
import com.springboot.project.service.transaction.model.CreateTransactionRequestModel;
import com.springboot.project.service.transaction.model.TransactionDetailModel;
import com.springboot.project.service.transaction.model.TransactionFilterRequestModel;
import com.springboot.project.service.transaction.model.TransactionFilterResponseModel;
import com.springboot.project.service.transaction.model.UpdateTransactionRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionDtoMapper {

    TransactionDtoMapper MAPPER = Mappers.getMapper(TransactionDtoMapper.class);

    TransactionFilterRequestModel toTransactionFilterRequestModel(
            TransactionFilterRequestDto requestDto);

    TransactionFilterResponseDto toTransactionFilterResponseDto(
            TransactionFilterResponseModel responseModel);

    TransactionDetailDto toTransactionDetailDto(TransactionDetailModel detailModel);

    CreateTransactionRequestModel toCreateTransactionRequestModel(
            CreateTransactionRequestDto requestDto);

    UpdateTransactionRequestModel toUpdateTransactionRequestModel(
            UpdateTransactionRequestDto requestDto);
}
