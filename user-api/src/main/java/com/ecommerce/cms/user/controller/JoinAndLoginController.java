package com.ecommerce.cms.user.controller;

import com.ecommerce.cms.user.domain.model.JoinForm;
import com.ecommerce.cms.user.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JoinAndLoginController {
    private final JoinService joinService;
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody JoinForm joinForm){
        System.out.println("들어옴>>>>name is>>"+joinForm.getName());
        return ResponseEntity.ok(joinService.Join(joinForm));
    }
    @PutMapping("/join/validate")
    public ResponseEntity<String> validate(String email, String code){
        return ResponseEntity.ok(joinService.validate(email,code));
    }

}
