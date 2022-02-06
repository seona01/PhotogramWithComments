package com.cos.photogramstart.config.auth;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service    // @ 붙이면 IoC가 된다
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userEntity = userRepository.findByUsername(username);  // 1.password는 spring security에서 체킹

        if(userEntity == null) {
            return null;
        } else {
            return new PrincipalDetails(userEntity);  // 2.리턴 잘되면 내부적으로 세션을 자동으로 만듬. UserDetails타입을 세션으로 만듬. user object 활용 가능
        }
    }
}
