package com.ecommerce.cms.user.service;

import com.ecommerce.cms.user.domain.entity.Customer;
import com.ecommerce.cms.user.domain.model.JoinForm;
import com.ecommerce.cms.user.domain.repository.CustomerRepository;
import com.ecommerce.cms.user.exception.CustomException;
import com.ecommerce.cms.user.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final CustomerRepository customerRepository;

    private final SendEmailService sendEmailService;

    public String Join(JoinForm joinForm){

        if(checkEmail(joinForm.getEmail())){
            throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
        }else{
            Customer customer = customerRepository.save(Customer.form(joinForm));
            String validateCode = sendEmailService.sendMail(customer);
            String name = "";

            if(!validateCode.isEmpty()){
                name = changeValidateCode(customer.getId(),validateCode);
            }
            return name + "님! 회원가입에 성공했습니다.";
        }

    }

    public boolean checkEmail(String email){
        return customerRepository.findByEmail(email).isPresent();
    }
    @Transactional
    public String changeValidateCode(Long id, String validateCode){
        Optional<Customer> c = customerRepository.findById(id);

        if(c.isPresent()){
            Customer customer = c.get();
            customer.setVerificationCode(validateCode);
            customer.setVerifyExpiredDate(LocalDateTime.now().plusDays(1));
            customerRepository.save(customer);
            return customer.getName();
        }
        throw new CustomException(ErrorCode.NOT_FOUND_USER);
    }
    @Transactional
    public String validate(String email, String code){
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_USER));
        if(customer.isVerify()){
            throw new CustomException(ErrorCode.ALREADY_CHECK_USER);
        } else if (customer.getVerifyExpiredDate().isBefore(LocalDateTime.now())){
            throw new CustomException(ErrorCode.EXPIRE_DATE);
        } else if (!customer.getVerificationCode().equals(code)) {
            throw new CustomException(ErrorCode.NOT_CORRECT_VALIDATION_CODE);
        }
        customer.setVerify(true);
        customerRepository.save(customer);
        return "이메일 인증이 완료되었습니다.";
    }
}
