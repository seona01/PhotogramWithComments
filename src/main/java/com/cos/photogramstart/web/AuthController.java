package com.cos.photogramstart.web;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.AuthService;
import com.cos.photogramstart.web.dto.auth.SignupDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor    // final field를 DI할때 사용. final걸려있는 서비스 생성자 생략하고.
@Controller // IoC등록의미, file return
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @GetMapping("/auth/signin")
    public String signinForm() {
        return "auth/signin";
    }

    @GetMapping("/auth/signup")
    public String signupForm() {
        return "auth/signup";
    }

    @PostMapping ("/auth/signup")
    public String signup(@Valid SignupDTO signupDTO, BindingResult bindingResult) {         // key=value (x-www-form-urlencoded  //String이라 "auth/signin" 파일형 리턴 // @Valid 아이디 길이 전처리 (DTO에서 Max, NotBlank, Size)   // @ResponseBody(controller지만) 자료형 앞에 붙으면 data 응답
        log.info(signupDTO.toString());

        User user = signupDTO.toEntity();
        log.info(user.toString());
        authService.register(user);   // db insert
//            System.out.println(userEntity);

        return "auth/signin";       // signup성공시 signin페이지로 redirect

    }
}


/*
클라이언트에서 회원 가입 요청하면
시큐리티는 CSRF 토큰 검사

응답 singup.jsp에 CSRF 토큰. input박스에 csrf="kfc"
 */