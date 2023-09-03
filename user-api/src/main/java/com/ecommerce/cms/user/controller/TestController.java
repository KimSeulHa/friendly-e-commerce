package com.ecommerce.cms.user.controller;

import com.ecommerce.cms.user.service.SendEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final SendEmailService sendEmailService;

    @GetMapping
    public void sendMail(){
        String response = sendEmailService.sendMail();
        System.out.println(response);
    }

}
