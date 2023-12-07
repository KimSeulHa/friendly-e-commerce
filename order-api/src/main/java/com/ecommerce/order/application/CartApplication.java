package com.ecommerce.order.application;

import com.ecommerce.order.domain.entity.Product;
import com.ecommerce.order.domain.entity.ProductItem;
import com.ecommerce.order.domain.model.AddProductCartForm;
import com.ecommerce.order.domain.redis.Cart;
import com.ecommerce.order.exception.CustomException;
import com.ecommerce.order.exception.ErrorCode;
import com.ecommerce.order.service.CartService;
import com.ecommerce.order.service.ProductSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
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

        //3.상품 가능 여부 체크
        if(cart != null && !addAble(cart,product,form)){
            throw new CustomException(ErrorCode.ITEM_COUNT_NOT_ENOUGH);
        }

        //4.장바구니 추가
        return cartService.addCart(customerId,form);
    }

    /**
     * 장바구니 가져오기
     * @param customerId
     * @return Cart
     */
    public Cart getCart(Long customerId){
        //1.장바구니 재조회
        Cart cart = refreshCart(cartService.getCart(customerId));

        //2.return을 위한 장바구니 생성
        Cart returnCart = new Cart();
        returnCart.setCustomerId(customerId);
        returnCart.setProducts(cart.getProducts());
        returnCart.setMsg(cart.getMsg());

        //3.레디스 message 삭제
        cart.setMsg(new ArrayList<>());
        cartService.putCart(customerId,cart);
        return returnCart;
    }

    /**
     * 장바구니 변경사항 조회
     * @param cart
     * @return
     */
    public Cart refreshCart(Cart cart){

        //고객 카트 상품id로 등록된 상품 DB list 가져오기 -> map으로 변환
        List<Long> productIds = cart.getProducts().stream()
                                .map(Cart.Product::getId).collect(Collectors.toList());
        Map<Long,Product> productMap = searchService.getProductsByProductIds(productIds)
                                    .stream().collect(Collectors.toMap(Product::getId,p->p));


        //상품 유효성 체크
        for(int i = 0; i < cart.getProducts().size(); i++){
            Cart.Product cartProduct = cart.getProducts().get(i);

            //1.상품의 존재여부 확인
            Product product = productMap.get(cartProduct.getId());
            if(product == null){
                cart.getProducts().remove(cartProduct);
                i--;
                cart.addMsg(cartProduct.getName()+"상품이 품절되어 장바구니에서 삭제 되었습니다.");
                continue;
            }

            //2.상품의 옵션 유효성 체크
            Map<Long, ProductItem> itemMap = product.getProductItems().stream()
                    .collect(Collectors.toMap(ProductItem::getId,productItem -> productItem));

            List<String> tempMsg = new ArrayList<>();
            for(int j = 0; j < cartProduct.getProductItems().size(); j++){
                Cart.ProductItem cartItem = cartProduct.getProductItems().get(j);
                ProductItem productItem = itemMap.get(cartItem.getId());

                //2-1.옵션 존재여부 확인
                if(productItem == null){
                    cartProduct.getProductItems().remove(cartItem);
                    j--;
                    cart.addMsg(cartProduct.getName()+"상품의 "+cartItem.getName()+" 품절되어 장바구니에서 삭제 되었습니다.");
                    continue;
                }
                //2-2.옵션 수량, 가격 확인하기
                boolean isPrice = false, isCount = false;
                if(!productItem.getPrice().equals(cartItem.getPrice())){
                    isPrice = true;
                    cartItem.setPrice(productItem.getPrice());
                }

                //주문 가능한 최대 수량으로 변경
                if(productItem.getCount() < cartItem.getCount()){
                    isCount = true;
                    cartItem.setCount(productItem.getCount());
                }

                if(isPrice && isCount){
                    tempMsg.add(cartProduct.getName()+"상품의 "+cartItem.getName()+" 옵션의 가격과 주문 가능 수량이 변동되었습니다.");
                }else if(isPrice){
                    tempMsg.add(cartProduct.getName()+"상품의 옵션의 가격이 변동되었습니다.");
                }else if(isCount){
                    if(cartItem.getCount() == 0){
                        System.out.println("들어옴");
                        cartProduct.getProductItems().remove(cartItem);
                    }else{
                        tempMsg.add(cartProduct.getName()+"상품의 주문 가능 수량이 변동되었습니다.");
                    }
                }
            }

            //3.상품의 옵션이 모두 존재하지 않아, 상품도 삭제하는 경우
            if(cartProduct.getProductItems().size() == 0){
                cart.getProducts().remove(cartProduct);
                i--;
                cart.addMsg(cartProduct.getName()+"상품이 품절되어 장바구니에서 삭제 되었습니다.");
                continue;
            }
            if(tempMsg.size() > 0){
                StringBuilder sb = new StringBuilder();
                sb.append(cartProduct.getName()+"상품의 정보가 변경되었습니다.");
                for(String msg : tempMsg){
                    sb.append(msg);
                    sb.append(",");
                }
                cart.addMsg(sb.toString());
            }
        }
        cartService.putCart(cart.getCustomerId(),cart);
        return cart;
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
    public Cart modifyCart(Long customerId, Cart cart){
        cartService.putCart(customerId,cart);
        return getCart(customerId);
    }

}
