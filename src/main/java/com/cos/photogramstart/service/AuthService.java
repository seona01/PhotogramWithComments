package com.cos.photogramstart.service;
// input 넘겨받은거 db등록
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service    // IoC등록, 트랜잭션 관리
public class AuthService {

    private final UserRepository userRepository;    // db 등록
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional  // 함수에 트랜잭션 관리. write(insert, update, delete)
    public User register(User user) {       // user:외부에서 받은 input값

        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);     // 암호화된 password(Hash)
        user.setPassword(encPassword);
        user.setRole("ROLE_USER");      // 관리자는 추후 ROLE_ADMIN

        User userEntity = userRepository.save(user);        // userEntity:db에 담을 값
        return userEntity;
    }
}
