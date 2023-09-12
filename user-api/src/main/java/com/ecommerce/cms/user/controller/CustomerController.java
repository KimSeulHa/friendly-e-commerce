package com.ecommerce.cms.user.controller;

import com.ecommerce.cms.user.domain.entity.Customer;
import com.ecommerce.cms.user.domain.model.customer.CustomerDto;
import com.ecommerce.cms.user.exception.CustomException;
import com.ecommerce.cms.user.exception.ErrorCode;
import com.ecommerce.cms.user.service.CustomerService;
import com.ecommerce.domain.common.UserVo;
import com.ecommerce.domain.config.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final CustomerService customerService;
    @GetMapping("/getInfo")
    public ResponseEntity<CustomerDto> getInfo(@RequestHeader(name = "X-AUTH-TOKEN")String token){
        UserVo userVo = jwtAuthenticationProvider.getUserVo(token);
        Customer customer =  customerService.findByIdAndEmail(userVo.getId(), userVo.getEmail()).orElseThrow(
                ()->new CustomException(ErrorCode.NOT_FOUND_USER)
        );
        return ResponseEntity.ok(CustomerDto.from(customer));
    }
}
