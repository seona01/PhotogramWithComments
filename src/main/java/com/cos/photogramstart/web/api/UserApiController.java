package com.cos.photogramstart.web.api;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.SubscribeService;
import com.cos.photogramstart.service.UserService;
import com.cos.photogramstart.web.dto.CMRespDTO;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDTO;
import com.cos.photogramstart.web.dto.user.UserUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController     // api는 data 응답하므로 전부 restController
public class UserApiController {

    private final UserService userService;      // DI
    private final SubscribeService subscribeService;

    @PutMapping("/api/user/{principalId}/profileImageUrl")
    public ResponseEntity<?> profileImageUrlUpdate(@PathVariable int principalId, MultipartFile profileImageFile, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        // MultipartFile ajax로 전송받을땐 profile.jsp의 input name 똑같이 받아야 업로드 가능
        // 세션 값이 바껴야해서 @AuthenticationPrincipal PrincipalDetails principalDetails도 받는다
        User userEntity = userService.updateProfileImg(principalId, profileImageFile);              // 변경된 userEntity로 받고
        principalDetails.setUser(userEntity);                                                       // 세션 변경
        return new ResponseEntity<>(new CMRespDTO<>(1, "프로필사진변경 성공", null), HttpStatus.OK);
    }

    @GetMapping("/api/user/{pageUserId}/subscribe")
    public ResponseEntity<?> subscribeList(@PathVariable int pageUserId, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        List<SubscribeDTO> subscribeDTO = subscribeService.slist(principalDetails.getUser().getId(), pageUserId);

        return new ResponseEntity<>(new CMRespDTO<>(1, "구독자 정보 리스트 불러오기 성공", subscribeDTO), HttpStatus.OK);
    }

    @PutMapping("/api/user/{id}")
    public CMRespDTO<?> update(@PathVariable int id, @Valid UserUpdateDTO userUpdateDTO, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        // @AuthenticationPrincipal PrincipalDetails정보수정 후 정보변경페이지 다시 들어가면 수정입력했던 input값 사라지는것 방지. 세션정보접근
        // BindingResult는 꼭 @Valid 다음 parameter로 입력
//        System.out.println(userUpdateDTO);


        User userEntity = userService.update(id, userUpdateDTO.toEntity());
        principalDetails.setUser(userEntity);           // 세션 정보 변경
        return new CMRespDTO<>(1, "회원수정완료", userEntity);            // 응답시 userEntity의 (내부적으로 messageConverter가 작동)모든 getter함수 호출되고 JSON으로 파싱하여 응답

    }
}
