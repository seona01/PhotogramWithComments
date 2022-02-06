package com.cos.photogramstart.config;

import com.cos.photogramstart.config.oauth.OAuth2DetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor        // OAuth2 DI
@Configuration      // IoC에 등록
@EnableWebSecurity  // 해당 파일로 security 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final OAuth2DetailsService oAuth2DetailsService;

    @Bean   // SecurityConfig가 콘테이너 등록될때 bean을 읽어서 비번 암호화
    public BCryptPasswordEncoder encode() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // super.configure(http); 여기때문에 localhost:8080에서 기존security로 redirect되므로 지우기(기존 기능 비활성화)

        http.csrf().disable();          // CSRF 비활성화(토큰 해제)
        http.authorizeRequests()
                .antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**", "/api/**").authenticated()
                .anyRequest().permitAll()       // 403 에러 : 접근 허가 못받은 경우
                .and()
                .formLogin()
                .loginPage("/auth/signin")      // 인증필요할때 login페이지로 redirect : get
                .loginProcessingUrl("/auth/signin")    // 로그인 요청 후 redirect : post (signin.jsp input method="post" 설정 후). 로그인컨트롤러는 위임하고 스프링 시큐리티가 로그인 프로세스 진행
                .defaultSuccessUrl("/")         // login 성공시 redirect
                .and()
                .oauth2Login()                  // form로그인 또는 oauth2 로그인도
                .userInfoEndpoint()             // oauth2로그인하게 될 경우 최종응답은 oauth2 회원정보 바로 받을 수 있다
                .userService(oAuth2DetailsService);
    }
}
