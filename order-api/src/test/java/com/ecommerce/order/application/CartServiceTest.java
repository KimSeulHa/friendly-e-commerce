package com.ecommerce.order.application;

import com.ecommerce.order.domain.entity.Product;
import com.ecommerce.order.domain.entity.ProductItem;
import com.ecommerce.order.domain.model.AddProductCartForm;
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

@SpringBootTest
class CartServiceTest {
    @Autowired
    private CartApplication cartApplication;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    //1.상품 추가 하기
    @Test
    Product ADD_PRODUCT_TEST(){
        Long sellerId = 1L;
        return productService.addProduct(sellerId,addProductForm("크록스","에코클로브",3));
    }

    //2.장바구니 추가
    @Test
    void ADD_AND_REFRESH_CART(){
        Product p = ADD_PRODUCT_TEST();
        Product result = productRepository.findWithProductItemsById(p.getId()).get();

        assertNotNull(result);

        assertEquals(result.getName(),"크록스");
        assertEquals(result.getDescription(),"에코클로브");
        assertEquals(result.getProductItems().size(),3);

        Long customerId = 1000L;

        cartApplication.addCart(customerId,addProductCart(result));


    }

    public static AddProductCartForm addProductCart(Product p){

        //1.아이템 추가
        List<AddProductCartForm.ProductItem> productItems = new ArrayList<>();
        for(int i = 0; i < p.getProductItems().size(); i++){
            ProductItem productItem = p.getProductItems().get(i);
            AddProductCartForm.ProductItem pi = AddProductCartForm.ProductItem.builder()
                    .id(productItem.getId())
                    .name(productItem.getName())
                    .price(productItem.getPrice())
                    .count(productItem.getCount())
                    .build();
            productItems.add(pi);
        }

        //2.상품 추가
        AddProductCartForm addProductCartForm = AddProductCartForm.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .productItems(productItems)
                .sellerId(p.getSellerId())
                .build();

        return addProductCartForm;
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
                .price(78000)
                .count(5)
                .build();
    }


}