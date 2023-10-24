package com.thanksd.server.exception.badrequest;

import com.thanksd.server.exception.ThanksdException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BadRequestException extends ThanksdException {

    public BadRequestException(String message, int code) {
        super(HttpStatus.BAD_REQUEST, message, code);
    }
}
