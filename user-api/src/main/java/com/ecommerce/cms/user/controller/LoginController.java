package com.ecommerce.cms.user.controller;

import com.ecommerce.cms.user.domain.model.LoginForm;
import com.ecommerce.cms.user.service.customer.LoginService;
import com.ecommerce.cms.user.service.seller.SellerLoginService;
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

    private final SellerLoginService sellerLoginService;

    @PostMapping("/customer")
    public ResponseEntity<String> customerLogin(@RequestBody LoginForm form){
        return ResponseEntity.ok(loginService.loginAndToken(form));
    }

    @PostMapping("/seller")
    public ResponseEntity<String> sellerLogin(@RequestBody LoginForm form){
        return ResponseEntity.ok(sellerLoginService.loginAndToken(form));
    }
}
