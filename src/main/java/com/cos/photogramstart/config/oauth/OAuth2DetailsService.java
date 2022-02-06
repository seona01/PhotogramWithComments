package com.cos.photogramstart.config.oauth;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService {        // 자료형 변경. security config오류해결

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        System.out.println("OAuth2 Service");
        OAuth2User oAuth2User = super.loadUser(userRequest);
//        System.out.println(oAuth2User.getAttributes());

        Map<String, Object> userInfo = oAuth2User.getAttributes();

        String username = "facebook_" + (String) userInfo.get("id");
        String password = new BCryptPasswordEncoder().encode(UUID.randomUUID().toString());
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");

        User userEntity = userRepository.findByUsername(username);

        if(userEntity == null){     // 페북 최초 로그인
            User user = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .name(name)
                    .role("ROLE_USER")
                    .build();

            return new PrincipalDetails(userRepository.save(user), oAuth2User.getAttributes());
        } else {                    // 페북통해 이미 회원가입한 유저
            return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
        }

    }
}
