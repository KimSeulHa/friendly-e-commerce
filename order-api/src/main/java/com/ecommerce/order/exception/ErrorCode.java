package com.ecommerce.order.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ALREADY_REGISTER_USER(HttpStatus.BAD_REQUEST,"이미 사용중인 이메일 입니다.");

    private final HttpStatus httpStatus;
    private final String detail;

}
