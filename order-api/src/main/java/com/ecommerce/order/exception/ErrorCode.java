package com.ecommerce.order.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ALREADY_ITEM_NAME(HttpStatus.BAD_REQUEST,"이미 사용중인 아이템명 입니다."),
    NOT_FOUND_PRODUCT_NAME(HttpStatus.BAD_REQUEST,"해당 상품은 존재하지 않습니다."),
    NOT_FOUND_PRODUCT_ITEM(HttpStatus.BAD_REQUEST,"해당 상품 아이템은 존재하지 않습니다."),
    ITEM_COUNT_NOT_ENOUGH(HttpStatus.BAD_REQUEST,"해당 상품의 수량이 부족합니다."),

    NOT_SAVE_CART(HttpStatus.BAD_REQUEST,"장바구니 저장에 실패했습니다."),

    ORDER_FAIL_CHECK_CART(HttpStatus.BAD_REQUEST,"주문 불가! 장바구니를 확인해주세요."),
    ORDER_FAIL_NOT_ENOUGH_BALANCE(HttpStatus.BAD_REQUEST,"주문 불가! 잔액이 부족합니다.");


    private final HttpStatus httpStatus;
    private final String detail;

}
