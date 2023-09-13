package com.ecommerce.cms.user.service.customer;

import com.ecommerce.cms.user.domain.entity.Customer;
import com.ecommerce.cms.user.domain.model.JoinForm;
import com.ecommerce.cms.user.domain.repository.CustomerRepository;
import com.ecommerce.cms.user.exception.CustomException;
import com.ecommerce.cms.user.exception.ErrorCode;
import com.ecommerce.cms.user.service.SendEmailService;
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

    /**
     * 사용자 입력 정보 DB저장 및 이메일 인증을 위한 인증코드 전송
     * @param joinForm
     * @return String
     */
    public String Join(JoinForm joinForm){
        //1. 이메일 유효성 체크
        if(checkEmail(joinForm.getEmail())){
            throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
        }else{
            //2. 입력정보 DB 저장
            Customer customer = customerRepository.save(Customer.form(joinForm));
            //3. 인증코드 생성 후 메일 전송
            String validateCode = sendEmailService.sendMail(customer);
            String name = "";

            if(!validateCode.isEmpty()){
                name = changeValidateCode(customer.getId(),validateCode);
            }
            return name + "님! 회원가입에 성공했습니다.";
        }

    }

    /**
     * 이메일 유효성 체크(UNIQUE)
     * @param email - String
     * @return boolean
     */
    public boolean checkEmail(String email){
        return customerRepository.findByEmail(email).isPresent();
    }

    /**
     * 이메일 인증 정보 저장
     * @param id - String
     * @param validateCode - String
     * @return String
     */
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
    /**
     * 이메일 인증을 통해 계정 정보에 로그인 활성화
     * @param email
     * @param code
     * @return String
     */
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
        //계정 활성화
        customer.setVerify(true);
        customerRepository.save(customer);
        return "이메일 인증이 완료되었습니다.";
    }
}
