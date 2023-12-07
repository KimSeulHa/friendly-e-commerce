package com.ecommerce.order.controller;

import com.ecommerce.domain.config.JwtAuthenticationProvider;
import com.ecommerce.order.application.CartApplication;
import com.ecommerce.order.domain.model.AddProductCartForm;
import com.ecommerce.order.domain.redis.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer/cart")
@RequiredArgsConstructor
public class CustomerCartController {

    private final CartApplication cartApplication;

    private final JwtAuthenticationProvider provider;
    @PostMapping("/addCart")
    public ResponseEntity<Cart> addCart(
            @RequestHeader(name="X-AUTH-TOKEN") String token,
            @RequestBody AddProductCartForm form){
        return ResponseEntity.ok(cartApplication.addCart(provider.getUserVo(token).getId(),form));
    }
    @GetMapping("/getCart")
    public ResponseEntity<Cart> getCart(@RequestHeader(name="X-AUTH-TOKEN") String token){
        return ResponseEntity.ok(cartApplication.getCart(provider.getUserVo(token).getId()));
    }

    @PutMapping("/modifyCart")
    public ResponseEntity<Cart> modifyCart(@RequestHeader(name="X-AUTH-TOKEN") String token,
                                           @RequestBody Cart cart){
        return ResponseEntity.ok(cartApplication.modifyCart(provider.getUserVo(token).getId(),cart));
    }

}
