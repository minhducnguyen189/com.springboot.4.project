package com.springboot.project.mapper;

import com.springboot.project.entity.LoginUserEntity;
import com.springboot.project.generated.model.LoginUserResponseModel;
import com.springboot.project.model.LoginUserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LoginUserMapper {

    LoginUserMapper MAPPER = Mappers.getMapper(LoginUserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    LoginUserEntity toLoginUserEntity(LoginUserModel loginUserModel);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateLoginUserEntity(
            @MappingTarget LoginUserEntity userEntity, LoginUserModel loginUserModel);

    LoginUserResponseModel toLoginUserResponseModel(LoginUserEntity loginUserEntity);
}
