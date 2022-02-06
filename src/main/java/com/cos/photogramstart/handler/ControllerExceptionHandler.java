package com.cos.photogramstart.handler;

import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.util.Script;
import com.cos.photogramstart.web.dto.CMRespDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController         // data 응답
@ControllerAdvice       // 모든 exception 처리
public class ControllerExceptionHandler {

    @ExceptionHandler(CustomValidationException.class)
    public String validationException(CustomValidationException e) {
        if(e.getErrorMap() == null) {
            return Script.back(e.getMessage());
        } else {
            return Script.back(e.getErrorMap().toString());
        }
    }   // 클라이언트에게 응답할 때는 script가 좋다 , Ajax통신과 android통신과 CMRespDto를 쓰게 된다

    @ExceptionHandler(CustomException.class)
    public String exception(CustomException e) {
        return Script.back(e.getMessage());
    }

    /*
    @ExceptionHandler(CustomValidationException.class)           // RuntimeException오류 모두 처리
    public CMRespDTO<?> validationException(CustomValidationException e) {      // 제너릭타입 리턴시 자료형 String, Map 대신 <?>로 처리 가능
        return new CMRespDTO<Map<String, String>>(-1, e.getMessage(), e.getErrorMap());        // ErrorMap으로 바꿔서 String이었던 Map<>형으로 바꿈 -> CMRespDTO만든후 자료형변경
    }
    */

    @ExceptionHandler(CustomValidationApiException.class)
    public ResponseEntity<?> validationApiException(CustomValidationApiException e) {
        return new ResponseEntity<>(new CMRespDTO<>(-1, e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
    }   // 클라이언트에게 응답할 때는 script가 좋다 , Ajax통신과 android통신과 CMRespDto를 쓰게 된다

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> apiException(CustomApiException e) {
        return new ResponseEntity<>(new CMRespDTO<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }   // 클라이언트에게 응답할 때는 script가 좋다 , Ajax통신과 android통신과 CMRespDto를 쓰게 된다
}
