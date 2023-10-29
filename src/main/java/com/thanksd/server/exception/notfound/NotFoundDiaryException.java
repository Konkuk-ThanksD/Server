package com.thanksd.server.exception.notfound;

public class NotFoundDiaryException extends NotFoundException{
    public NotFoundDiaryException() {
        super("존재하지 않는 일기입니다.",2000);
    }
}
