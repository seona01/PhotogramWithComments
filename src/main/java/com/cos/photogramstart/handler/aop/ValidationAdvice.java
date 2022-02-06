package com.cos.photogramstart.handler.aop;

import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Component      // RestController, Service 모든것들이 Component 상속해서 만들어져 있음
@Aspect         // 공통기능
public class ValidationAdvice {

    @Around("execution(* com.cos.photogramstart.web.api.*Controller.*(..))")
    public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        System.out.println("web api Controller*************");
        Object[] args = proceedingJoinPoint.getArgs();
        for(Object arg:args){
            if(arg instanceof BindingResult){
                System.out.println("유효성 검사를 하는 함수입니다");
                BindingResult bindingResult = (BindingResult) arg;

                // if문 복사 후 다른 API Controller에 있는 if문들 중복 제거
                if(bindingResult.hasErrors()) {
                    Map<String, String> errorMap = new HashMap<>();

                    for(FieldError error : bindingResult.getFieldErrors()){         // getFieldError 리스트에 담는
                        errorMap.put(error.getField(), error.getDefaultMessage());
                        // System.out.println(error.getDefaultMessage());
                    }
                    throw new CustomValidationApiException("유효성검사 실패", errorMap);            // 문자열 넣을 수 있게. CommentApiController도 CustomValidationApiException 처리
                }
            }
//            System.out.println(args);
        }
        return proceedingJoinPoint.proceed();      // apiController함수 이때 실행
    }

    @Around("execution(* com.cos.photogramstart.web.*Controller.*(..))")
    public Object advice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        System.out.println("web controller-------------");
        Object[] args = proceedingJoinPoint.getArgs();
        for(Object arg:args){
            if(arg instanceof BindingResult){
                System.out.println("유효성 검사를 하는 함수입니다");
                BindingResult bindingResult = (BindingResult) arg;

                if(bindingResult.hasErrors()) {
                    Map<String, String> errorMap = new HashMap<>();

                    for(FieldError error : bindingResult.getFieldErrors()){         // getFieldError 리스트에 담는
                        errorMap.put(error.getField(), error.getDefaultMessage());
                        System.out.println(error.getDefaultMessage());
                    }
                    throw new CustomValidationException("유효성검사 실패", errorMap);            // 문자열 넣을 수 있게
                }
            }
//            System.out.println(args);
        }
        return proceedingJoinPoint.proceed();
    }
}
