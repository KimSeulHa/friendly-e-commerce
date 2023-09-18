package com.ecommerce.order.service;

import com.ecommerce.order.domain.entity.Product;
import com.ecommerce.order.domain.entity.ProductItem;
import com.ecommerce.order.domain.model.AddProductItemForm;
import com.ecommerce.order.domain.model.UpdateProductItemForm;
import com.ecommerce.order.domain.repository.ProductItemRepository;
import com.ecommerce.order.domain.repository.ProductRepository;
import com.ecommerce.order.exception.CustomException;
import com.ecommerce.order.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductItemService {
    private final ProductRepository productRepository;
    private final ProductItemRepository productItemRepository;

    /**
     * 상품 아이템 추가
     * @param sellerId
     * @param form
     * @return Product
     */
    @Transactional
    public Product addItem(Long sellerId, AddProductItemForm form){
        Product product = productRepository.findBySellerIdAndId(sellerId, form.getProductId())
                .orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_PRODUCT_NAME));

        if(product.getProductItems().stream()
                .anyMatch(item -> item.getName().equals(form.getName()))){
            throw new CustomException(ErrorCode.ALREADY_ITEM_NAME);
        }

        ProductItem productItem = ProductItem.of(sellerId,form);
        product.getProductItems().add(productItem);

        return product;

    }

    /**
     * 상품 아이템 수정
     * @param sellerId
     * @param form
     * @return ProductItem
     */
    @Transactional
    public ProductItem updateItem(Long sellerId, UpdateProductItemForm form){
        ProductItem productItem = productItemRepository.findById(form.getId())
                .filter(item -> sellerId.equals(item.getSellerId())).orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_PRODUCT_ITEM));

        productItem.setName(form.getName());
        productItem.setCount(form.getCount());
        productItem.setPrice(form.getPrice());

        return productItem;
    }

}
