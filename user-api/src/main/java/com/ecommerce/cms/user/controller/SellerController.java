package com.ecommerce.cms.user.controller;

import com.ecommerce.cms.user.domain.entity.Seller;
import com.ecommerce.cms.user.domain.model.customer.CustomerDto;
import com.ecommerce.cms.user.domain.model.seller.SellerDto;
import com.ecommerce.cms.user.exception.CustomException;
import com.ecommerce.cms.user.exception.ErrorCode;
import com.ecommerce.cms.user.service.customer.JoinService;
import com.ecommerce.cms.user.service.seller.SellerService;
import com.ecommerce.domain.common.UserVo;
import com.ecommerce.domain.config.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final SellerService sellerService;
    private final JoinService joinService;

    /**
     * Token을 통해 사용자 정보 가져오기
     * @param token - String
     * @return SellerDto
     */
    @GetMapping("/getInfo")
    public ResponseEntity<SellerDto> getInfo(@RequestHeader(name = "X-AUTH-TOKEN")String token){
        UserVo userVo = jwtAuthenticationProvider.getUserVo(token);
        Seller seller =  sellerService.findByIdAndEmail(userVo.getId(), userVo.getEmail()).orElseThrow(
                ()->new CustomException(ErrorCode.NOT_FOUND_USER)
        );
        return ResponseEntity.ok(SellerDto.from(seller));
    }
}
