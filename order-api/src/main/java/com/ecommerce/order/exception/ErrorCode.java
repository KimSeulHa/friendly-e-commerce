package com.ecommerce.order.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ALREADY_ITEM_NAME(HttpStatus.BAD_REQUEST,"이미 사용중인 아이템명 입니다."),
    NOT_FOUND_PRODUCT_NAME(HttpStatus.BAD_REQUEST,"해당 상품은 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String detail;

}
