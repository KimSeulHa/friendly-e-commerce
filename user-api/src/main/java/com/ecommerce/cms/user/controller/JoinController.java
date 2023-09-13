package com.ecommerce.cms.user.controller;

import com.ecommerce.cms.user.domain.model.JoinForm;
import com.ecommerce.cms.user.service.customer.JoinService;
import com.ecommerce.cms.user.service.seller.SellerJoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/join")
public class JoinController {

    private final JoinService joinService;

    private final SellerJoinService sellerJoinService;

    @PostMapping("/customer")
    public ResponseEntity<String> customerJoin(@RequestBody JoinForm joinForm){

        return ResponseEntity.ok(joinService.Join(joinForm));
    }

    @PostMapping("/seller")
    public ResponseEntity<String> sellerJoin(@RequestBody JoinForm joinForm){

        return ResponseEntity.ok(sellerJoinService.Join(joinForm));
    }

    /**
     * Email 인증 for Customer
     * @param email - String
     * @param code - String
     * @return String
     * */
    @GetMapping("/customer/validate")
    public ResponseEntity<String> validateCustomer(String email, String code){
        return ResponseEntity.ok(joinService.validate(email,code));
    }

    /**
     * Email 인증 for Seller
     * @param email - String
     * @param code - String
     * @return String
     * */
    @GetMapping("/seller/validate")
    public ResponseEntity<String> validateSeller(String email, String code){
        return ResponseEntity.ok(sellerJoinService.validate(email,code));
    }

}
