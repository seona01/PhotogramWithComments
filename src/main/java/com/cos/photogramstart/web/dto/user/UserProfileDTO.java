package com.cos.photogramstart.web.dto.user;

import com.cos.photogramstart.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder                // 가입 input db저장
@AllArgsConstructor     // 전체 생성자
@NoArgsConstructor      // bean 생성자
@Data
public class UserProfileDTO {

    private boolean pageOwnerState;
    private int imageCount;
    private boolean subscribeState;
    private int subscribeCount;
    private User user;
}
