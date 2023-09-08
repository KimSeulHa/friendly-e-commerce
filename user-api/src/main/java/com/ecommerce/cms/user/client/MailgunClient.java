package com.ecommerce.cms.user.client;

import com.ecommerce.cms.user.client.mailgun.SendMailForm;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(name = "mailgun", url= "https://api.mailgun.net/v3/")
@Qualifier("mailgun")
public interface MailgunClient {

    @PostMapping("sandboxb67a6e7962e449498e8f0e0efb49f0e5.mailgun.org/messages")
    ResponseEntity<String> sendMail(@SpringQueryMap SendMailForm form);
    //Feign에서 사용하는 Resonse를 사용

}
