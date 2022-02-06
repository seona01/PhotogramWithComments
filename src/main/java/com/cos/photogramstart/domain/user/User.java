package com.cos.photogramstart.domain.user;
// JPA : Java Persistence API 자바로 데이터를 영구적으로 저장할 수 있는 API 제공
// db insert
import com.cos.photogramstart.domain.image.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder                // 가입 input db저장
@AllArgsConstructor     // 전체 생성자
@NoArgsConstructor      // bean 생성자
@Data
@Entity
public class User {
                                                        // AoP 관점지향프로그래밍 -> 전처리 후처리
    @Id                                                 // PK:후처리 exception으로 해결
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincre
    private Integer id;

    @Column(length = 40, unique = true)                 // length:전처리 validation으로 해결
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    private String website;
    private String bio;
    @Column(nullable = false)
    private String email;
    private String phone;
    private String gender;
    private String profileImageUrl;
    private String role;

    /*
    image클래스 변수 넣어줌. 연관관계 주인 아님 명시. 테이블에 칼럼 생성하지 않음.
    user select할때 해당 user id로 등록된 image 전부 불러옴
    Lazy = User를 select할때 해당 user id로 등록된 image 불러오지 않음. 대신 getImages()함수 호출시 불러옴
    Eager = User를 select할때 해당 user id로 등록된 image들 join해서 불러옴
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"user"})                         // 양방향 매핑시 무한참조(서로 계속 호출) 막기
    private List<Image> images;                             // 양방향 매핑

    private LocalDate createDate;   // 데이터 들어온 날짜.

    @PrePersist // db에 insert직전 실행. 자동으로
    public void createDate() {
        this.createDate = LocalDate.from(LocalDateTime.now());
    }
}
