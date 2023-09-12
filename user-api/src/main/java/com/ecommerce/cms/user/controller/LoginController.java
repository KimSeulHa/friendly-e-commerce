package com.ecommerce.cms.user.controller;

import com.ecommerce.cms.user.domain.model.LoginForm;
import com.ecommerce.cms.user.service.LoginService;
import com.ecommerce.domain.config.JwtAuthenticationProvider;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/login")
public class LoginController {

    private final LoginService loginService;
    @PostMapping
    public ResponseEntity<String> login(@RequestBody LoginForm form){
        return ResponseEntity.ok(loginService.login(form));
    }
}
