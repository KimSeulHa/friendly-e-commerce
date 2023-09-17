package com.ecommerce.order.service;

import com.ecommerce.order.domain.entity.Product;
import com.ecommerce.order.domain.model.AddProductForm;
import com.ecommerce.order.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    /**
     * 상품 추가
     * @param sellerId
     * @param form
     * @return
     */
    @Transactional
    public Product addProduct(Long sellerId, AddProductForm form){
        return productRepository.save(Product.of(sellerId,form));
    }
}
