package com.cos.photogramstart.web.api;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.service.ImageService;
import com.cos.photogramstart.service.LikesService;
import com.cos.photogramstart.web.dto.CMRespDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController         // api는 data 응답하므로 전부 restController
public class ImageApiController {

    private final ImageService imageService;
    private final LikesService likesService;

    @GetMapping("/api/image")
    public ResponseEntity<?> imageStory(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                        @PageableDefault(size=3) Pageable pageable) {
        Page<Image> images = imageService.mainImages(principalDetails.getUser().getId(), pageable);
        return new ResponseEntity<>(new CMRespDTO<>(1, "성공", images), HttpStatus.OK);
    }

    @PostMapping("/api/image/{imageId}/likes")
    public ResponseEntity<?> likes(@PathVariable int imageId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        likesService.likes(imageId, principalDetails.getUser().getId());
        return new ResponseEntity<>(new CMRespDTO<>(1, "좋아요성공", null), HttpStatus.CREATED);
    }

    @DeleteMapping ("/api/image/{imageId}/likes")
    public ResponseEntity<?> unlikes(@PathVariable int imageId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        likesService.unlikes(imageId, principalDetails.getUser().getId());
        return new ResponseEntity<>(new CMRespDTO<>(1, "좋아요취소완료", null), HttpStatus.OK);
    }
}
