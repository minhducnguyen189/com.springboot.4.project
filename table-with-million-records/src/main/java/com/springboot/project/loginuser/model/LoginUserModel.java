package com.springboot.project.loginuser.model;

import com.springboot.project.loginuser.entity.UserRoleEnumEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginUserModel {

    private String email;
    private String username;
    private String name;
    private List<UserRoleEnumEntity> roles;
}
