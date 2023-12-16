package com.ecommerce.order.application;

import com.ecommerce.order.client.UserClient;
import com.ecommerce.order.client.customer.BalanceDto;
import com.ecommerce.order.client.customer.CustomerDto;
import com.ecommerce.order.domain.entity.ProductItem;
import com.ecommerce.order.domain.redis.Cart;
import com.ecommerce.order.exception.CustomException;
import com.ecommerce.order.service.ProductItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.IntStream;

import static com.ecommerce.order.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class OrderApplication {

    /**
     * 결제를 위한 STEP
     * 1STEP. 상품들이 모두 주문 가능한지
     * 2STEP. 가격 변동이 있었는지
     * 3STEP. 고객의 돈이 충분한지
     * 4STEP. 결제 & 상품의 재고 관리
     */

    private final CartApplication cartApplication;
    private final UserClient userClient;
    private final ProductItemService productItemService;
    public void order(String token, Cart cart){
        //STEP OF 1 AND 2
        Cart orderCart = cartApplication.refreshCart(cart);
        if(orderCart.getMsg().size() > 0){
            throw new CustomException(ORDER_FAIL_CHECK_CART);
        }

        //3STEP
        CustomerDto customerDto = userClient.getInfo(token).getBody();
        int totalCost = getCartTotalCost(orderCart);
        if(customerDto.getBalance() < totalCost){
            throw new CustomException(ORDER_FAIL_NOT_ENOUGH_BALANCE);
        }
        //3-1STEP. 만약에 결제하려고 하는데,
        //잔액에 돈이 빠져나가서 돈이 없는 경우에 롤백의 상황을 고려해야함
        userClient.changeBalance(token,BalanceDto.builder()
                .description("Order"+ LocalDate.now())
                .from(customerDto.getEmail())
                .money(-totalCost)
                .build());

        for(Cart.Product cartProduct : orderCart.getProducts()){
            for(Cart.ProductItem cartItem: cartProduct.getProductItems()){
                ProductItem productItem = productItemService.getProductItem(cartItem.getId());
                productItem.setCount(productItem.getCount()-cartItem.getCount());
            }
        }

    }

    public int getCartTotalCost(Cart cart){
        return cart.getProducts().stream().flatMapToInt(
                product -> product.getProductItems().stream().flatMapToInt(
                        productItem -> IntStream.of(productItem.getPrice() * productItem.getCount())))
                .sum();
    }

}
