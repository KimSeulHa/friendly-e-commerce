package com.ecommerce.order.application;

import com.ecommerce.order.domain.entity.Product;
import com.ecommerce.order.domain.model.AddProductCartForm;
import com.ecommerce.order.domain.model.AddProductForm;
import com.ecommerce.order.domain.model.AddProductItemForm;
import com.ecommerce.order.domain.redis.Cart;
import com.ecommerce.order.domain.repository.ProductRepository;
import com.ecommerce.order.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 장바구니 등록 및 가져오기 test
 */
@SpringBootTest
class CartApplicationTest {
    @Autowired
    private CartApplication cartApplication;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @Test
    void ADD_PRODUCT_AND_REFRESH(){
        Product product = add_product_test();

        Long customerId = 1000L;
        Cart cart = cartApplication.addCart(customerId,addProductCartForm(product,5));
        //잘 들어갔는지 given when then

        Cart getcart = cartApplication.getCart(customerId);
        assertEquals(getcart.getMsg().size(),1);
        System.out.println(getcart.getMsg()+""+getcart.getMsg().size());

    }

    AddProductCartForm addProductCartForm(Product p,int cnt){
        AddProductCartForm.ProductItem productItem
                = AddProductCartForm.ProductItem.builder()
                .name(p.getProductItems().get(0).getName())
                .id(p.getProductItems().get(0).getId())
                .count(cnt)
                .price(p.getProductItems().get(0).getPrice()*cnt)
                .build();
        return AddProductCartForm.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .productItems(List.of(productItem))
                .build();
    }



    /**
     * step1. Add product
     * @return
     */
    Product add_product_test(){
        Long sellerId = 1L;
        Product product = productService.addProduct(sellerId,addProductForm("나이키 조던","범고래",3));
        return product;
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
                .count(10)
                .build();
    }

}