package com.thanksd.server.exception.badrequest;

public class InvalidDateException extends BadRequestException {

    public InvalidDateException() {
        super("유효하지 않은 연도 혹은 월 입니다.", 2004);
    }
}
