package com.ecommerce.order.application.service;

import com.ecommerce.order.domain.entity.Product;
import com.ecommerce.order.domain.model.AddProductForm;
import com.ecommerce.order.domain.model.AddProductItemForm;
import com.ecommerce.order.domain.repository.ProductRepository;
import com.ecommerce.order.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 상품 추가 test
 */
@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Test
    void ADD_PRODUCT_TEST(){
        Long sellerId = 1L;

        Product product = productService.addProduct(sellerId,addProductForm("나이키 조던","범고래",3));
        assertNotNull(product);
        assertEquals(product.getProductItems().size(),3);
        assertEquals(product.getProductItems().get(0).getName(),"나이키 조던0");
        assertEquals(product.getProductItems().get(0).getPrice(),1000);
        assertEquals(product.getProductItems().get(0).getCount(),1);
    }
    public static AddProductForm addProductForm(String name, String description, int cnt){
        List<AddProductItemForm> itemFormList = new ArrayList<>();
        for(int i = 0; i <cnt; i++){
            itemFormList.add(makePIForm(null, name+i));
        }
        return AddProductForm.builder()
                .name(name)
                .description(description)
                .productItemForms(itemFormList)
                .build();
    }
    public static AddProductItemForm makePIForm(Long productId, String name){
        return AddProductItemForm.builder()
                .productId(productId)
                .name(name)
                .price(1000)
                .count(1)
                .build();
    }

}