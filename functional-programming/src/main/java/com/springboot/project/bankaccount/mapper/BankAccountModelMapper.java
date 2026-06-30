package com.springboot.project.bankaccount.mapper;

import com.springboot.project.bankaccount.entity.BankAccountEntity;
import java.util.List;

import com.springboot.project.bankaccount.model.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BankAccountModelMapper {

        BankAccountModelMapper MAPPER = Mappers.getMapper(BankAccountModelMapper.class);

        @BeanMapping(ignoreByDefault = true)
        @Mapping(target = "firstName", source = "firstName")
        @Mapping(target = "lastName", source = "lastName")
        @Mapping(target = "phone", source = "phone")
        @Mapping(target = "email", source = "email")
        @Mapping(target = "accountNumber", source = "accountNumber")
        @Mapping(target = "accountType", source = "accountType")
        @Mapping(target = "ifscCode", source = "ifscCode")
        @Mapping(target = "status", source = "status")
        BankAccountEntity toBankAccountEntityFromExample(
                        BankAccountFilterRequestModel filterRequestModel);

        BankAccountDetailModel toBankAccountDetail(BankAccountEntity bankAccountEntity);

        List<BankAccountDetailModel> toBankAccountDetails(List<BankAccountEntity> bankAccountEntities);

        BankAccountEntity toBankAccountEntity(CreateBankAccountRequestModel requestModel);

        BankAccountDetailModel toBankAccountDetailFromRequest(CreateBankAccountRequestModel requestModel);

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "sequenceNumber", ignore = true)
        @Mapping(target = "transactions", ignore = true)
        @Mapping(target = "firstName", source = "firstName")
        @Mapping(target = "lastName", source = "lastName")
        @Mapping(target = "phone", source = "phone")
        @Mapping(target = "email", source = "email")
        @Mapping(target = "street", source = "street")
        @Mapping(target = "streetNumber", source = "streetNumber")
        @Mapping(target = "postalCode", source = "postalCode")
        @Mapping(target = "city", source = "city")
        @Mapping(target = "country", source = "country")
        @Mapping(target = "accountNumber", source = "accountNumber")
        @Mapping(target = "accountType", source = "accountType")
        @Mapping(target = "ifscCode", source = "ifscCode")
        @Mapping(target = "balance", source = "balance")
        @Mapping(target = "currency", source = "currency")
        @Mapping(target = "status", source = "status")
        void updateBankAccountEntity(UpdateBankAccountRequestModel requestModel, @MappingTarget BankAccountEntity entity);
}
