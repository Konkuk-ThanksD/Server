package com.thanksd.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response<D> {

    private String code;
    private String message;
    private D data;

    public static <D> Response<D> ofSuccess(String message, D data) {
        return new Response<>("200", "OK", data);
    }
}
