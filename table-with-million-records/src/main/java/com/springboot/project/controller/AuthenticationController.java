package com.springboot.project.controller;

import com.springboot.project.generated.api.AuthenticationApi;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("deprecation")
@RestController
public class AuthenticationController implements AuthenticationApi {

    @Override
    public ResponseEntity<Void> authenticationLogin(@Nullable String redirectPath) {
        // TODO: implement authentication login redirect
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> refreshToken() {
        // TODO: implement refresh token redirect
        return ResponseEntity.ok().build();
    }
}
