package com.springboot.project.service.transaction.mapper;

import com.springboot.project.dao.entity.TransactionDetailEntity;
import com.springboot.project.service.transaction.model.CreateTransactionRequestModel;
import com.springboot.project.service.transaction.model.TransactionDetailModel;
import com.springboot.project.service.transaction.model.TransactionFilterRequestModel;
import com.springboot.project.service.transaction.model.UpdateTransactionRequestModel;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionModelMapper {

    TransactionModelMapper MAPPER = Mappers.getMapper(TransactionModelMapper.class);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "date", source = "date")
    @Mapping(target = "domain", source = "domain")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "value", source = "value")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "paymentMethod", source = "paymentMethod")
    @Mapping(target = "taxAmount", source = "taxAmount")
    @Mapping(target = "netValue", source = "netValue")
    TransactionDetailEntity toTransactionDetailEntityFromExample(
            TransactionFilterRequestModel filterRequestModel);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "date", source = "date")
    @Mapping(target = "domain", source = "domain")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "value", source = "value")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "paymentMethod", source = "paymentMethod")
    @Mapping(target = "taxAmount", source = "taxAmount")
    @Mapping(target = "netValue", source = "netValue")
    TransactionDetailEntity toTransactionDetailEntity(
            CreateTransactionRequestModel createTransactionRequestModel);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "date", source = "date")
    @Mapping(target = "domain", source = "domain")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "value", source = "value")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "paymentMethod", source = "paymentMethod")
    @Mapping(target = "taxAmount", source = "taxAmount")
    @Mapping(target = "netValue", source = "netValue")
    void updateTransactionDetailEntity(
            UpdateTransactionRequestModel updateTransactionRequestModel,
            @MappingTarget TransactionDetailEntity transactionDetailEntity);

    @Mapping(source = "bankAccount.id", target = "bankAccountId")
    TransactionDetailModel toTransactionDetail(
            TransactionDetailEntity transactionDetailEntity);

    @Mapping(source = "bankAccount.id", target = "bankAccountId")
    List<TransactionDetailModel> toTransactionDetails(
            List<TransactionDetailEntity> transactionDetailEntities);
}
