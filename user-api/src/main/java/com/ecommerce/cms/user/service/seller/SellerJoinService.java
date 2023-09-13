package com.ecommerce.cms.user.service.seller;

import com.ecommerce.cms.user.domain.entity.Seller;
import com.ecommerce.cms.user.domain.model.JoinForm;
import com.ecommerce.cms.user.domain.repository.SellerRepository;
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
public class SellerJoinService {

    private final SellerRepository sellerRepository;

    private final SendEmailService sendEmailService;

    /**
     * 사용자 입력 정보 DB저장 및 이메일 인증을 위한 인증코드 전송
     * @param joinForm
     * @return String
     */
    public String Join(JoinForm joinForm){
        if(checkEmail(joinForm.getEmail())){
            throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
        }else{
            Seller seller = sellerRepository.save(Seller.form(joinForm));
            String validateCode = sendEmailService.SellerSendMail(seller);
            String name = "";

            if(!validateCode.isEmpty()){
                name = changeValidateCode(seller.getId(),validateCode);
            }
            return name + "님! 회원가입에 성공했습니다.";
        }
    }

    /**
     * 이메일 유효성 체크(UNIQUE)
     * @param email
     * @return boolean
     */
    public boolean checkEmail(String email){
        return sellerRepository.findByEmail(email).isPresent();
    }

    /**
     * 이메일 인증 정보 저장
     * @param id
     * @param validateCode
     * @return String
     */
    public String changeValidateCode(Long id, String validateCode){
        Optional<Seller> s = sellerRepository.findById(id);

        if(s.isPresent()){
            Seller seller = s.get();
            seller.setVerificationCode(validateCode);
            seller.setVerifyExpiredDate(LocalDateTime.now().plusDays(1));
            sellerRepository.save(seller);
            return seller.getName();
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
        Seller seller = sellerRepository.findByEmail(email)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_USER));
        if(seller.isVerify()){
            throw new CustomException(ErrorCode.ALREADY_CHECK_USER);
        } else if (seller.getVerifyExpiredDate().isBefore(LocalDateTime.now())){
            throw new CustomException(ErrorCode.EXPIRE_DATE);
        } else if (!seller.getVerificationCode().equals(code)) {
            throw new CustomException(ErrorCode.NOT_CORRECT_VALIDATION_CODE);
        }
        //계정 활성화
        seller.setVerify(true);
        sellerRepository.save(seller);
        return "이메일 인증이 완료되었습니다.";
    }
}
