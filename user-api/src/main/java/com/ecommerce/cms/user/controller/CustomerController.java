package com.ecommerce.cms.user.controller;

import com.ecommerce.cms.user.domain.entity.BalanceHistory;
import com.ecommerce.cms.user.domain.entity.Customer;
import com.ecommerce.cms.user.domain.model.customer.BalanceDto;
import com.ecommerce.cms.user.domain.model.customer.CustomerDto;
import com.ecommerce.cms.user.exception.CustomException;
import com.ecommerce.cms.user.exception.ErrorCode;
import com.ecommerce.cms.user.service.customer.BalanceService;
import com.ecommerce.cms.user.service.customer.CustomerService;
import com.ecommerce.cms.user.service.customer.JoinService;
import com.ecommerce.domain.common.UserVo;
import com.ecommerce.domain.config.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final CustomerService customerService;
    private final BalanceService balanceService;
    private final JoinService joinService;


    /**
     * Token을 통해 사용자 정보 가져오기
     * @param token - String
     * @return CustomerDto
     */
    @GetMapping("/getInfo")
    public ResponseEntity<CustomerDto> getInfo(@RequestHeader(name = "X-AUTH-TOKEN")String token){
        UserVo userVo = jwtAuthenticationProvider.getUserVo(token);
        Customer customer =  customerService.findByIdAndEmail(userVo.getId(), userVo.getEmail()).orElseThrow(
                ()->new CustomException(ErrorCode.NOT_FOUND_USER)
        );
        return ResponseEntity.ok(CustomerDto.from(customer));
    }

    /**
     * 적립금 충전 및 사용하기
     * @param token
     * @param balanceDto
     * @return String
     */
    @PostMapping("/balance")
    public ResponseEntity<String> changeBalance(@RequestHeader(name = "X-AUTH-TOKEN")String token,
                                                @RequestBody BalanceDto balanceDto){
        UserVo userVo = jwtAuthenticationProvider.getUserVo(token);

        BalanceHistory balanceHistory = balanceService.changeBalance(userVo.getId(),balanceDto);
        return ResponseEntity.ok(balanceDto.getMoney()+"원을 충전(차감)해서\n"+
                "총 잔액은 "+balanceHistory.getBalance()+"원 입니다.");
    }
}
