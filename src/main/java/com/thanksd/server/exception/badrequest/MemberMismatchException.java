package com.thanksd.server.exception.badrequest;

public class MemberMismatchException extends BadRequestException{
    public MemberMismatchException() {
        super("일기는 작성자만 접근 가능합니다.",2002);
    }
}
