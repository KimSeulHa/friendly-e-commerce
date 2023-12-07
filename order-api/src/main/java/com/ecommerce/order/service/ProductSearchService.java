package com.ecommerce.order.service;

import com.ecommerce.order.domain.entity.Product;
import com.ecommerce.order.domain.repository.ProductRepository;
import com.ecommerce.order.domain.repository.ProductRepositoryCustom;
import com.ecommerce.order.exception.CustomException;
import com.ecommerce.order.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ProductRepositoryCustom productRepositoryCustom;
    private final ProductRepository productRepository;

    public List<Product> searchByName(String name){
        return productRepositoryCustom.searchByName(name);
    }

    public Product getItemsByProductId(Long productId){
        return productRepository.findWithProductItemsById(productId)
                .orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_PRODUCT_ITEM));
    }

    public List<Product> getProductsByProductIds(List<Long> productIds){
        return productRepository.findAllByIdIn(productIds);
    }
}
