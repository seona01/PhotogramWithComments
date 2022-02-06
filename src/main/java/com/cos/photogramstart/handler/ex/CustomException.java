package com.cos.photogramstart.handler.ex;

import java.util.Map;

public class CustomException extends RuntimeException{

    // serial은 객체 구분할 때 사용. JVM에만 중요한 개념
    private static final long serialVersionUID = 1L;

    public CustomException(String message) {
        super(message);
    }

    // message getter : super(message)로 던져 처리
}
