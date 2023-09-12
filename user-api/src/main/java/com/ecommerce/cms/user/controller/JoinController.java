package com.ecommerce.cms.user.controller;

import com.ecommerce.cms.user.domain.model.JoinForm;
import com.ecommerce.cms.user.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/join")
public class JoinController {
    private final JoinService joinService;
    @PostMapping
    public ResponseEntity<String> join(@RequestBody JoinForm joinForm){

        return ResponseEntity.ok(joinService.Join(joinForm));
    }
    @PutMapping("/validate")
    public ResponseEntity<String> validate(String email, String code){
        return ResponseEntity.ok(joinService.validate(email,code));
    }

}
