package com.ecommerce.order.controller;

import com.ecommerce.domain.config.JwtAuthenticationProvider;
import com.ecommerce.order.domain.entity.ProductItem;
import com.ecommerce.order.domain.model.*;
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

    /**
     * 상품 추가
     * @param token
     * @param form
     * @return ResponseEntity<ProductDto>
     */
    @PostMapping("/addProduct")
    public ResponseEntity<ProductDto> addProduct(@RequestHeader(name="X-AUTH-TOKEN") String token,
                                           @RequestBody AddProductForm form){
        return ResponseEntity.ok(ProductDto.from(productService.addProduct(provider.getUserVo(token).getId(),form)));

    }

    /**
     * 상품 아이템 추가
     * @param token
     * @param form
     * @return ResponseEntity<ProductDto>
     */
    @PostMapping("/addItem")
    public ResponseEntity<ProductDto> addItem(@RequestHeader(name="X-AUTH-TOKEN") String token,
                                                  @RequestBody AddProductItemForm form){
        return ResponseEntity.ok(ProductDto.from(productItemService.addItem(provider.getUserVo(token).getId(),form)));
    }

    /**
     * 상품 수정
     * @param token
     * @param form
     * @return ResponseEntity<ProductDto>
     */
    @PutMapping("/updateProduct")
    public ResponseEntity<ProductDto> updateProduct(@RequestHeader(name="X-AUTH-TOKEN") String token,
                                                 @RequestBody UpdateProductForm form){
        return ResponseEntity.ok(ProductDto.from(productService.updateProduct(provider.getUserVo(token).getId(),form)));

    }

    /**
     * 상품 아이템 수정
     * @param token
     * @param form
     * @return ResponseEntity<ProductItemDto>
     */
    @PutMapping("/updateItem")
    public ResponseEntity<ProductItemDto> updateItem(@RequestHeader(name="X-AUTH-TOKEN") String token,
                                                  @RequestBody UpdateProductItemForm form){
        return ResponseEntity.ok(ProductItemDto.from(productItemService.updateItem(provider.getUserVo(token).getId(),form)));
    }
}
