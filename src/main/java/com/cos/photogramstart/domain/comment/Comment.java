package com.cos.photogramstart.domain.comment;

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
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // autoincre
    private int id;

    @Column(length = 100, nullable = false)
    private String content;

    @JsonIgnoreProperties({"images"})
    @JoinColumn(name="userId")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @JoinColumn(name="imageId")
    @ManyToOne(fetch = FetchType.EAGER)
    private Image image;

    private LocalDate createDate;   // 데이터 들어온 날짜.

    @PrePersist // db에 insert직전 실행. 자동으로
    public void createDate() {
        this.createDate = LocalDate.from(LocalDateTime.now());
    }
}
