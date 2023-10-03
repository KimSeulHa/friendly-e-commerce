package com.ecommerce.order.service;

import com.ecommerce.order.client.RedisClient;
import com.ecommerce.order.domain.model.AddProductCartForm;
import com.ecommerce.order.domain.redis.Cart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
    private final RedisClient redisClient;

    public Cart getCart(Long customerId){
        return redisClient.get(customerId, Cart.class);
    }
    public Cart addCart(Long customerId, AddProductCartForm form){
        Cart cart = redisClient.get(customerId,Cart.class);
        if(cart == null){
            //등록된 상품이 없는 경우
            cart = new Cart();
            cart.setCustomerId(customerId);
        }
        Optional<Cart.Product> OptionalProduct = cart.getProducts().stream()
                .filter(product -> product.getId().equals(form.getId())).findFirst();

        if(OptionalProduct.isPresent()){
            //이미 있는 상품
            Cart.Product redisProduct = OptionalProduct.get();
            List<Cart.ProductItem> redisItems = form.getProductItems().stream().map(Cart.ProductItem::from).collect(Collectors.toList());

            Map<Long,Cart.ProductItem> redisMap = redisProduct.getProductItems().stream()
                    .collect(Collectors.toMap(Cart.ProductItem::getId,item->item));

            //동일 정보가 아닌 경우
            if(!redisProduct.getName().equals(form.getName())){
                cart.addMsg(redisProduct.getName()+"의 정보가 변경되었습니다.");
            }

            for(Cart.ProductItem item : redisItems){
                Cart.ProductItem redisItem = redisMap.get(item.getId());

                //장바구니가 비어있을 경우
                if(redisItem == null){
                    redisProduct.getProductItems().add(item);
                }else{
                    if(!redisItem.getPrice().equals(item.getPrice())){
                        cart.addMsg(redisProduct.getName()+"의 가격이 변동되었습니다.");
                    }
                    redisItem.setCount(redisItem.getCount()+item.getCount());
                }
            }
        }else {
            Cart.Product product = Cart.Product.from(form);
            cart.getProducts().add(product);
            redisClient.put(customerId,cart);
            return cart;
        }
        redisClient.put(customerId,cart);
        return cart;
    }

}
