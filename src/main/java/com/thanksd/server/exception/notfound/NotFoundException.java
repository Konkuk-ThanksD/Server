package com.thanksd.server.exception.notfound;

import com.thanksd.server.exception.ThanksdException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends ThanksdException {

    public NotFoundException(String message, int code) {
        super(HttpStatus.NOT_FOUND, message, code);
    }
}
