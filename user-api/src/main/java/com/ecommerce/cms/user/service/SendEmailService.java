package com.ecommerce.cms.user.service;

import com.ecommerce.cms.user.client.MailgunClient;
import com.ecommerce.cms.user.client.mailgun.SendMailForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor //final이 붙은 필드의 생성자를 자동 주입
public class SendEmailService {

    private final MailgunClient mailgunClient;

    public String sendMail() {
        SendMailForm form = SendMailForm.builder()
                .from("kimsha21@naver.com")
                .to("kimsha21@naver.com")
                .subject("test")
                .text("test")
                .build();
        return mailgunClient.sendMail(form).getBody();
    }


}
