package com.springboot.project.controller;

import com.springboot.project.generated.api.LoginUserApi;
import com.springboot.project.generated.model.LoginUserResponseModel;
import com.springboot.project.service.LoginUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginUserController implements LoginUserApi {

    private final LoginUserService loginUserService;

    @Autowired
    public LoginUserController(LoginUserService loginUserService) {
        this.loginUserService = loginUserService;
    }

    @Override
    public ResponseEntity<LoginUserResponseModel> getCurrentLoginUser() {
        LoginUserResponseModel response = loginUserService.getCurrentLoginUser();
        return ResponseEntity.ok(response);
    }
}
