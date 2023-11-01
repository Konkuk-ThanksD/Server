package com.thanksd.server.exception.badrequest;

public class InvalidImageUrlException extends BadRequestException {
    public InvalidImageUrlException() {
        super("올바르지 않은 이미지 경로입니다.", 2003);
    }
}
