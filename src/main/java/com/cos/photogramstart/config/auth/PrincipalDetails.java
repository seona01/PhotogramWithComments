package com.cos.photogramstart.config.auth;

import com.cos.photogramstart.domain.user.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


// User object 담을 용도
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private static final long serialVersionUID = 1L;

    private User user;
    private Map<String, Object> attributes;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {        // 권한 가져오는 함수
        // return user.getRole();                                              // 권한이 1개가 아닐 수 있어서 collection

        Collection<GrantedAuthority> collector = new ArrayList<>();
        /* 복잡하게 작성하는 경우 -> 람다식으로
        collector.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        */
        collector.add(() -> {           // 람다식 구현. add 안에 함수를 넘기고 싶어서
            return user.getRole();
        });
        return collector;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;                            // false 떠있으면 로그인 불가능. 계정 로그인 1년 지나면 만기되는 경우 등
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {            // 10년 동안 로그인 안한 경우
        return true;
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
