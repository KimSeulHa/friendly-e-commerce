package com.ecommerce.order.controller;

import com.ecommerce.domain.config.JwtAuthenticationProvider;
import com.ecommerce.order.domain.model.AddProductForm;
import com.ecommerce.order.domain.model.AddProductItemForm;
import com.ecommerce.order.domain.model.ProductDto;
import com.ecommerce.order.domain.model.ProductItemDto;
import com.ecommerce.order.service.ProductItemService;
import com.ecommerce.order.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/seller/product")
public class SellerProductController {
    private final ProductService productService;
    private final ProductItemService productItemService;
    private final JwtAuthenticationProvider provider;

    @PostMapping("/addProduct")
    public ResponseEntity<ProductDto> addProduct(@RequestHeader(name="X-AUTH-TOKEN") String token,
                                           @RequestBody AddProductForm form){
        return ResponseEntity.ok(ProductDto.from(productService.addProduct(provider.getUserVo(token).getId(),form)));

    }
    @PostMapping("/addItem")
    public ResponseEntity<ProductDto> addItem(@RequestHeader(name="X-AUTH-TOKEN") String token,
                                                  @RequestBody AddProductItemForm form){
        return ResponseEntity.ok(ProductDto.from(productItemService.addItem(provider.getUserVo(token).getId(),form)));
    }
}
