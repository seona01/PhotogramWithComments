package com.cos.photogramstart.domain.image;

import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.likes.Likes;
import com.cos.photogramstart.domain.user.User;
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
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // autoincre
    private int id;
    private String caption;
    private String postImageUrl;    // 업로드 포토는 서버 특정 폴더에 저장하게 되는데 db에 그 경로 insert

    @JsonIgnoreProperties({"images"})
    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @JsonIgnoreProperties({"image"})    // 무한참조 막기
    @OneToMany(mappedBy = "image")      // 양방향 매핑. 이미지 들고올때 좋아요 상태도 함께 가져오기. likes getter호출시
    private List<Likes> likes;          // 하나의 이미지에 여러개의 좋아요 라서 oneToMany

    @OrderBy("id DESC")                 // 코멘트 등록 순서는 image.java에서 persistence OrderBy로 설정
    @JsonIgnoreProperties({"image"})
    @OneToMany(mappedBy = "image")      // FK에 대한 변수 적기. 연관관계 주인 아님
    private List<Comment> comments;     // 콜렉션은 양방향매핑

    private LocalDateTime createDate;   // 데이터 들어온 날짜.

    @Transient                          // persistence : DB에 컬럼 만들어지지 않는다
    private boolean likeState;

    @Transient
    private int likeCount;

    @PrePersist // db에 insert직전 실행. 자동으로
    public void createDate() {
        this.createDate = LocalDateTime.now();
    }

}
