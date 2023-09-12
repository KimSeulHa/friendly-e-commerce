package com.ecommerce.cms.user.service;

import com.ecommerce.cms.user.domain.entity.Customer;
import com.ecommerce.cms.user.domain.model.LoginForm;
import com.ecommerce.cms.user.domain.repository.CustomerRepository;
import com.ecommerce.cms.user.exception.CustomException;
import com.ecommerce.cms.user.exception.ErrorCode;
import com.ecommerce.domain.common.UserType;
import com.ecommerce.domain.config.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final CustomerRepository customerRepository;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    public String login(LoginForm form){
        //1. 아이디 가져오기
        Customer customer = getUserInfo(form.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        //2. 계정이 유효한지 확인하기
        if(!customer.isVerify()){
            throw new CustomException(ErrorCode.VERIFY_IS_FALSE);
        }
        if(!customer.getPasswd().equals(form.getPassword())){
            System.out.println("입력 비밀번호"+form.getPassword());
            System.out.println("DB 비밀번호"+customer.getPasswd());
            throw new CustomException(ErrorCode.NOT_CORRECT_LOGIN_INFO);
        }

        String token = jwtAuthenticationProvider.createToken(customer.getEmail(),customer.getId(), UserType.CUSTOMER);
        System.out.println("token is ....."+token);
        return "로그인에 성공했습니다.";
    }
    public Optional<Customer> getUserInfo(String email){
        return customerRepository.findByEmail(email).stream().findFirst();
    }
}
