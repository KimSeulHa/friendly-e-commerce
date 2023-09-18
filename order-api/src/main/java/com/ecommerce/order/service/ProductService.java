package com.ecommerce.order.service;

import com.ecommerce.order.domain.entity.Product;
import com.ecommerce.order.domain.entity.ProductItem;
import com.ecommerce.order.domain.model.AddProductForm;
import com.ecommerce.order.domain.model.UpdateProductForm;
import com.ecommerce.order.domain.model.UpdateProductItemForm;
import com.ecommerce.order.domain.repository.ProductRepository;
import com.ecommerce.order.exception.CustomException;
import com.ecommerce.order.exception.ErrorCode;
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
     * @return Product
     */
    @Transactional
    public Product addProduct(Long sellerId, AddProductForm form){
        return productRepository.save(Product.of(sellerId,form));
    }

    /**
     * 상품 수정
     * @param sellerId
     * @param form
     * @return Product
     */
    @Transactional
    public Product updateProduct(Long sellerId, UpdateProductForm form){
        Product product = productRepository.findBySellerIdAndId(sellerId,form.getId())
                .orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_PRODUCT_NAME));
        product.setName(form.getName());
        product.setDescription(form.getDescription());

        for(UpdateProductItemForm itemForm : form.getProductItemForms()){
            ProductItem productItem = product.getProductItems().stream()
                    .filter(item -> itemForm.getId().equals(item.getId()))
                    .findFirst().orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_PRODUCT_ITEM));
            productItem.setName(itemForm.getName());
            productItem.setPrice(itemForm.getPrice());
            productItem.setCount(itemForm.getCount());
        }
        return product;
    }

    /**
     * 상품 삭제
     * @param sellerId
     * @param id
     * @return String
     */
    @Transactional
    public String deleteProduct(Long sellerId, Long id){
        Product product = productRepository.findBySellerIdAndId(sellerId,id)
                .orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_PRODUCT_NAME));
        String name = product.getName();
        productRepository.delete(product);

        return name+"이 삭제되었습니다.";
    }
}
