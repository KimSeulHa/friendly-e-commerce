package com.ecommerce.cms.user.service.seller;

import com.ecommerce.cms.user.domain.entity.Seller;
import com.ecommerce.cms.user.domain.model.LoginForm;
import com.ecommerce.cms.user.domain.repository.SellerRepository;
import com.ecommerce.cms.user.exception.CustomException;
import com.ecommerce.cms.user.exception.ErrorCode;
import com.ecommerce.domain.common.UserType;
import com.ecommerce.domain.config.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerLoginService {

    private final SellerRepository sellerRepository;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    /**
     * 로그인 및 토큰 생성
     * @param form LoginForm
     * @return String
     */
    public String loginAndToken(LoginForm form){
        //1. 아이디 가져오기
        Seller seller = getUserInfo(form.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        //2. 계정이 유효한지 확인하기
        if(!seller.isVerify()){
            throw new CustomException(ErrorCode.VERIFY_IS_FALSE);
        }
        if(!seller.getPasswd().equals(form.getPassword())){
            throw new CustomException(ErrorCode.NOT_CORRECT_LOGIN_INFO);
        }

        //3.토큰 생성하기
        String token = jwtAuthenticationProvider.createToken(seller.getEmail(), seller.getId(), UserType.SELLER);
        System.out.println("token is ....."+token);
        return "로그인에 성공했습니다.";
    }

    /**
     * 이메일로 으로 사용자 정보 가져오기
     * @param email
     * @return Seller
     */
    public Optional<Seller> getUserInfo(String email){
        return sellerRepository.findByEmail(email).stream().findFirst();
    }
}
