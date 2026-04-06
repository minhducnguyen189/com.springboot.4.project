package com.springboot.project.service.bank_account.mapper;

import com.springboot.project.dao.entity.BankAccountEntity;
import java.util.List;

import com.springboot.project.service.bank_account.model.BankAccountDetailModel;
import com.springboot.project.service.bank_account.model.BankAccountFilterRequestModel;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
}
