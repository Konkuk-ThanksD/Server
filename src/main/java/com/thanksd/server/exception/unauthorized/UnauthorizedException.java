package com.thanksd.server.exception.unauthorized;

import com.thanksd.server.exception.ThanksdException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ThanksdException {

    public UnauthorizedException(String message, int code) {
        super(HttpStatus.UNAUTHORIZED, message, code);
    }
}
