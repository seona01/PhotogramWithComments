package com.cos.photogramstart.service;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.web.dto.image.ImageUploadDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor        // DI
@Service
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional(readOnly = true)
    public List<Image> popularImg() {
        return imageRepository.mPopular();
    }

    @Transactional(readOnly = true)             // readOnly=true 영속성 컨텍스트 변경감지해서 더티체킹하고 DB flush 반영X. @transactional걸어서 세션을 컨트롤러단까지 끌고오는 것 중요
    public Page<Image> mainImages(int principalId, Pageable pageable) {
        Page<Image> images = imageRepository.mStory(principalId, pageable);

        // image에 좋아요상태 담기. 이중for문
        images.forEach((image) -> {
            image.setLikeCount(image.getLikes().size());

            image.getLikes().forEach((like) -> {
                if(like.getUser().getId() == principalId) {         // 해당 이미지 좋아요 한 사람들 찾아서 현재 로그인한 사람이 좋아요 한것인지 비교
                    image.setLikeState(true);
                }
            });
        });
        return images;
    }

    @Value("${file.path}")      // yml에 path가져옴
    private String uploadFolder;

    @Transactional  // 서비스에서 db변형줄때 꼭
    public void upload(ImageUploadDTO imageUploadDTO, PrincipalDetails principalDetails) {      // void 리턴 값 줄 것 없다. exception은 handler에서 처리
        UUID uuid = UUID.randomUUID();      // UUID(Universally Unique Identifier) : 네트워크 상 고유성이 보장되는 id를 만들기 위한 표준 규약
        String imageFileName = uuid + "_" + imageUploadDTO.getFile().getOriginalFilename();
        // System.out.println("image file : " + imageFileName);

        Path imageFilePath = Paths.get(uploadFolder + imageFileName);

        // 통신, I/O 가져올때 예외발생 가능
        try {
            Files.write(imageFilePath, imageUploadDTO.getFile().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 이미지 db에 저장
        Image image = imageUploadDTO.toEntity(imageFileName, principalDetails.getUser());
        imageRepository.save(image);
        // Image imageEntity = imageRepository.save(image); // 받을필요없음
        // System.out.println(imageEntity);
    }
}
