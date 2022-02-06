package com.cos.photogramstart.service;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.web.dto.user.UserProfileDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final SubscribeRepository subscribeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;  // password 암호화 필수
    @Value("${file.path}")      // yml에 path가져옴
    private String uploadFolder;

    @Transactional
    public User updateProfileImg(int principalId, MultipartFile profileImageFile) {
        // ImageService에서 db저장 전과 같음
        UUID uuid = UUID.randomUUID();      // UUID(Universally Unique Identifier) : 네트워크 상 고유성이 보장되는 id를 만들기 위한 표준 규약
        String imageFileName = uuid + "_" + profileImageFile.getOriginalFilename();

        Path imageFilePath = Paths.get(uploadFolder + imageFileName);

        // 통신, I/O 가져올때 예외발생 가능
        try {
            Files.write(imageFilePath, profileImageFile.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        User userEntity = userRepository.findById(principalId).orElseThrow(()->{
            throw new CustomApiException("유저를 찾을 수 없습니다");          // 데이터를 리턴하므로 CustomApiException (스크립트 실행되면 안된다)
        });
        userEntity.setProfileImageUrl(imageFileName);
        return userEntity;
    }       // 더티체킹으로 업데이트

    @Transactional(readOnly = true)
    public UserProfileDTO userProfile(int pageUserId, int principalId) {       // 유저별 {id}로 셀렉트
        UserProfileDTO dto = new UserProfileDTO();

        // SELECT * FROM image WHERE userId = :userId; 쿼리로 할 거면 이 쿼리 이용
        User userEntity = userRepository.findById(pageUserId).orElseThrow(()-> {            // 검색이 안될수도 있어서 optional처리
            throw new CustomException("해당 프로필 페이지는 존재하지 않습니다");
        });

        dto.setUser(userEntity);
        dto.setPageOwnerState(pageUserId == principalId);          // pageowner1 or -1      // 같으면 true, 다르면 false
        dto.setImageCount(userEntity.getImages().size());

        int subscribeState = subscribeRepository.mSubscribeState(principalId, pageUserId);
        int subscribeCount = subscribeRepository.mSubscribeCount(pageUserId);
        dto.setSubscribeState(subscribeState == 1);
        dto.setSubscribeCount(subscribeCount);

        userEntity.getImages().forEach((image)-> {
            image.setLikeCount(image.getLikes().size());
        });

        // userEntity.getImages().get(0);
        return dto;
    }

    @Transactional
    public User update(int id, User user) {
        /*
        1. 영속화
        wrapping클래스에 optional<>을 붙임 1.무조건찾음get() 2.못찾을경우exception발생 : orElseThrow()
         */
        //User userEntity = userRepository.findById().get();        // 찾은 객체 userEntity에 담긴 후 영속화 case1
        User userEntity = userRepository.findById(id).orElseThrow(()-> {        // case2
            return new CustomValidationApiException("찾을 수 없는 Id입니다");
        });

        // 2. 영속화된 object 수정 - 더티체킹(업데이트 완료)
        userEntity.setName(user.getName());

        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);

        userEntity.setPassword(encPassword);
        userEntity.setBio(user.getBio());
        userEntity.setWebsite(user.getWebsite());
        userEntity.setPhone(user.getPhone());
        userEntity.setGender(user.getGender());
        return userEntity;
    }
}
