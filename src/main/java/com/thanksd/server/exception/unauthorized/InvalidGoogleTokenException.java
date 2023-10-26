package com.thanksd.server.exception.unauthorized;

public class InvalidGoogleTokenException extends UnauthorizedException {

    public InvalidGoogleTokenException() {
        super("올바르지 않은 구글 코드입니다.", 1011);
    }
}
