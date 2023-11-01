package com.thanksd.server.exception.badrequest;

public class InvalidImageNameException extends BadRequestException {
    public InvalidImageNameException() {
        super("유효하지 않은 이미지 형식입니다.", 3000);
    }
}
