package com.thanksd.server.exception.badrequest;

public class InvalidNationException extends BadRequestException {

    public InvalidNationException() {
        super("국가 정보가 올바르지 않습니다.", 1003);
    }
}
