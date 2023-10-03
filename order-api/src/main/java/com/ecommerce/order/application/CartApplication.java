package com.ecommerce.order.application;

import com.ecommerce.order.domain.entity.Product;
import com.ecommerce.order.domain.entity.ProductItem;
import com.ecommerce.order.domain.model.AddProductCartForm;
import com.ecommerce.order.domain.redis.Cart;
import com.ecommerce.order.exception.CustomException;
import com.ecommerce.order.exception.ErrorCode;
import com.ecommerce.order.service.CartService;
import com.ecommerce.order.service.ProductSearchService;
import com.ecommerce.order.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartApplication {
    private final ProductSearchService searchService;
    private final CartService cartService;

    /**
     * 장바구니 유효성체크 및 추가
     * @param customerId
     * @param form
     * @return cart
     */
    public Cart addCart(Long customerId, AddProductCartForm form){

        //1.제품 존재 여부
        Product product = searchService.getItemsByProductId(form.getId());
        if(product == null){
            throw new CustomException(ErrorCode.NOT_FOUND_PRODUCT_NAME);
        }

        //2.상품 수량 확인
        Cart cart = cartService.getCart(customerId);
        if(cart != null && !addAble(cart,product,form)){
            throw new CustomException(ErrorCode.ITEM_COUNT_NOT_ENOUGH);
        }

        //3.장바구니 추가
        return cartService.addCart(customerId,form);
    }

    /**
     * 장바구니 추가 가능여부 확인(수량 체크)
     * @param cart
     * @param product
     * @param form
     * @return
     */
    private boolean addAble(Cart cart,Product product, AddProductCartForm form) {
        //1.Redis 장바구니에 있는 제품 ?????????
        Cart.Product cartProduct = cart.getProducts().stream().filter(p -> p.getId().equals(form.getId()))
                .findFirst().orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT_NAME));

        //2.
        Map<Long, Integer> cartItemCount = cartProduct.getProductItems().stream()
                .collect(Collectors.toMap(Cart.ProductItem::getId, Cart.ProductItem::getCount));

        //3.
        Map<Long, Integer> ableItemCount = product.getProductItems().stream()
                .collect(Collectors.toMap(ProductItem::getId, ProductItem::getCount));


        return form.getProductItems().stream().noneMatch(
                productItem -> {
                    Integer cartCount = cartItemCount.get(productItem.getId());
                    Integer ableCount = ableItemCount.get(productItem.getId());
                    return (productItem.getCount() + cartCount > ableCount);
                });
    }

}
