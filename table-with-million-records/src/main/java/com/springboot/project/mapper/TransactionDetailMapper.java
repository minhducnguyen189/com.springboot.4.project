package com.springboot.project.mapper;

import com.springboot.project.entity.TransactionDetailEntity;
import com.springboot.project.generated.model.CreateTransactionRequestModel;
import com.springboot.project.generated.model.TransactionDetailModel;
import com.springboot.project.generated.model.TransactionFilterRequestModel;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionDetailMapper {

        TransactionDetailMapper MAPPER = Mappers.getMapper(TransactionDetailMapper.class);

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

        @Mapping(source = "bankAccount.id", target = "bankAccountId")
        TransactionDetailModel toTransactionDetail(TransactionDetailEntity transactionDetailEntity);

        @Mapping(source = "bankAccount.id", target = "bankAccountId")
        List<TransactionDetailModel> toTransactionDetails(
                        List<TransactionDetailEntity> transactionDetailEntities);
}
