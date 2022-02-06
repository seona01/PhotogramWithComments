package com.cos.photogramstart.web.dto.subscribe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Builder                // 가입 input db저장
@AllArgsConstructor     // 전체 생성자
@NoArgsConstructor      // bean 생성자
@Data
public class SubscribeDTO {

    private int id;
    private String username;
    private String profileImageUrl;
    private BigInteger SubscribeState;
    private BigInteger equalUserState;     // 구독정보에 로그인한 유저의 정보가 오면 안되서
}
