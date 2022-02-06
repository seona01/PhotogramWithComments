package com.cos.photogramstart.handler.ex;

import java.util.Map;

public class CustomValidationException extends RuntimeException{

    // serial은 객체 구분할 때 사용. JVM에만 중요한 개념
    private static final long serialVersionUID = 1L;

    private String message;
    private Map<String, String> errorMap;

    public CustomValidationException(String message, Map<String, String> errorMap) {
        super(message);
        this.errorMap = errorMap;
    }

    public Map<String, String> getErrorMap() {
        return errorMap;
    }

    // message getter : super(message)로 던져 처리
}
