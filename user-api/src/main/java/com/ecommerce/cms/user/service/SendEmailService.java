package com.ecommerce.cms.user.service;

import com.ecommerce.cms.user.client.MailgunClient;
import com.ecommerce.cms.user.client.mailgun.SendMailForm;
import com.ecommerce.cms.user.domain.entity.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Slf4j
@Service
@RequiredArgsConstructor //final이 붙은 필드의 생성자를 자동 주입
public class SendEmailService {

    private final MailgunClient mailgunClient;

    public String sendMail(Customer customer) {
        String vaildationCode = makeRandomCode();
        String emailText = makeEmailForm(customer, vaildationCode);
        SendMailForm form = SendMailForm.builder()
                .from("kimsha21@naver.com")
                .to(customer.getEmail())
                .subject("mail for verification")
                .text(emailText)
                .build();
        log.info("sendEmail result is.."+mailgunClient.sendMail(form).toString());

        return vaildationCode;
    }

    public String makeRandomCode(){
        return RandomStringUtils.random(10,true,true);
    }
    public String makeEmailForm(Customer customer,String validationCode){
        StringBuilder stringBuilder = new StringBuilder();

        return stringBuilder.append("Hello, ").append(customer.getName()).append("!\n")
                .append("please click link for verify your email!")
                .append("http://localhost:8081/customer/join?email=")
                .append(customer.getEmail())
                .append("&code=")
                .append(validationCode).toString();

    }

}
