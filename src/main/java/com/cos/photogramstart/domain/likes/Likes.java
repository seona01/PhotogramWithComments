package com.cos.photogramstart.domain.likes;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder                // 가입 input db저장
@AllArgsConstructor     // 전체 생성자
@NoArgsConstructor      // bean 생성자
@Data
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "likes_uk",
                        columnNames = {"imageId", "userId"}         // 두개의 컬럼에 중복값 들어올 수 없도록 unique key 설정
                )
        }
)
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // autoincre
    private int id;

    @JoinColumn(name = "imageId")
    @ManyToOne
    private Image image;            // 하나의 이미지는 여러개의 like. 1:N관계


    @JsonIgnoreProperties({"images"})
    @JoinColumn(name = "userId")
    @ManyToOne
    private User user;              // 한명의 유저는 like여러번 할 수 있다. 1:N관계

    private LocalDate createDate;   // 데이터 들어온 날짜.

    @PrePersist // db에 insert직전 실행. 자동으로
    public void createDate() {
        this.createDate = LocalDate.from(LocalDateTime.now());
    }
}
