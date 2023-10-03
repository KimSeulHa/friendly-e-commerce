package com.ecommerce.cms.user.service;

import com.ecommerce.cms.user.client.MailgunClient;
import com.ecommerce.cms.user.client.mailgun.SendMailForm;
import com.ecommerce.cms.user.domain.entity.Customer;
import com.ecommerce.cms.user.domain.entity.Seller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendEmailService {

    private final MailgunClient mailgunClient;

    /**
     * 회원 인증을 위한 이메일 전송 for customer
     * @param customer
     * @return String - 생성된 인증코드
     */
    public String sendMail(Customer customer) {
        //1. 인증코드 생성
        String validationCode = makeRandomCode();
        //2. 메일폼 생성
        String emailText = makeEmailForm(customer.getEmail(), customer.getName(), validationCode, "customer");
        //3. 메일 전송
        SendMailForm form = SendMailForm.builder()
                .from("kimsha21@naver.com")
                .to(customer.getEmail())
                .subject("mail for verification")
                .text(emailText)
                .build();
        log.info("sendEmail result is.."+mailgunClient.sendMail(form).toString());
        
        //메일 전송에 실패됐을 경우를 위해 exception 생성
        
        return validationCode;
    }

    /**
     * 회원 인증을 위한 이메일 전송 for Seller
     * @param seller
     * @return String - 생성된 인증코드
     */
    public String SellerSendMail(Seller seller) {
        //1. 인증코드 생성
        String validationCode = makeRandomCode();
        //2. 메일폼 생성
        String emailText = makeEmailForm(seller.getEmail(), seller.getName(),validationCode, "customer");
        //3. 메일 전송
        SendMailForm form = SendMailForm.builder()
                .from("kimsha21@naver.com")
                .to(seller.getEmail())
                .subject("mail for verification")
                .text(emailText)
                .build();
        log.info("sendEmail result is.."+mailgunClient.sendMail(form).toString());

        return validationCode;
    }

    /**
     * 인증 코드 생성
     * @return String
     */
    public String makeRandomCode(){
        return RandomStringUtils.random(10,true,true);
    }

    /**
     * 메일폼 생성
     * @param email
     * @param name
     * @param validationCode
     * @param type
     * @return String
     */
    public String makeEmailForm(String email, String name, String validationCode, String type){
        StringBuilder stringBuilder = new StringBuilder();

        return stringBuilder.append("Hello, ").append(name).append("!\n")
                .append("please click link for verify your email!")
                .append("http://localhost:8081/join/"+type+"validate?email=")
                .append(email)
                .append("&code=")
                .append(validationCode).toString();

    }

}
