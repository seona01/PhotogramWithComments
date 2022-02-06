package com.cos.photogramstart.web;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.ImageService;
import com.cos.photogramstart.web.dto.image.ImageUploadDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ImageController {

    private final ImageService imageService;

    @GetMapping({"/", "/image/story"})
    public String story() {
        return "image/story";
    }

    @GetMapping({"/image/popular"})
    public String popluar(Model model) {
        // api는 데이터를 리턴하는 서버. 안드로이드 IOS요청도 api에서. ajax를 할 것이 아니라 apiController가 아닌 ImageController에서 구현
        List<Image> images = imageService.popularImg();
        model.addAttribute("images", images);
        return "image/popular";
    }

    @GetMapping({"/image/upload"})
    public String upload() {
        return "image/upload";
    }

    @PostMapping("/image")
    public String imageUpload(ImageUploadDTO imageUploadDTO, @AuthenticationPrincipal PrincipalDetails principalDetails) {

        if(imageUploadDTO.getFile().isEmpty()) {
            throw new CustomValidationException("이미지가 첨부되지 않았습니다", null);       // 페이지를 응답하기 때문에 CustomValidationException 처리
        }

        // 이미지 등록. 서비스 호출
        imageService.upload(imageUploadDTO, principalDetails);
        // 등록 후 리턴 주소
        return "redirect:/user/" + principalDetails.getUser().getId();
    }
}
