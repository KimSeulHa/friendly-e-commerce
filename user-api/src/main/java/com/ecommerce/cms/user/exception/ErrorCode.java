package com.ecommerce.cms.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //ErrorCode for Join
    ALREADY_REGISTER_USER(HttpStatus.BAD_REQUEST,"이미 사용중인 이메일 입니다."),
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST ,"해당 사용자가 없습니다."),
    NOT_CORRECT_VALIDATION_CODE(HttpStatus.BAD_REQUEST ,"인증번호가 틀립니다."),
    ALREADY_CHECK_USER(HttpStatus.BAD_REQUEST ,"이미 인증된 계정입니다."),
    EXPIRE_DATE(HttpStatus.BAD_REQUEST ,"계정확인을 위한 유효일자가 지났습니다."),

    //ErrorCode for Login
    NOT_CORRECT_LOGIN_INFO(HttpStatus.BAD_REQUEST ,"비밀번호를 다시 확인해주세요."),
    VERIFY_IS_FALSE(HttpStatus.BAD_REQUEST ,"계정을 인증해주세요."),

    //ErrorCode for balance
    INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST ,"잔액이 부족합니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
