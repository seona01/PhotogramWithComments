package com.cos.photogramstart.domain.subscribe;

import com.cos.photogramstart.domain.user.User;
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
                        name = "subscribe_uk",
                        columnNames = {"fromUserId", "toUserId"}
                )
        }
)
public class Subscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // autoincre
    private Integer id;

    @JoinColumn(name = "fromUserId")  // 자동으로 테이블 컬럼에 _ 생성 방지
    @ManyToOne
    private User fromUser;      // 구독하는 사람

    @JoinColumn(name = "toUserId")
    @ManyToOne
    private User toUser;        // 구독당하는 사람

    private LocalDate createDate;   // 데이터 들어온 날짜.

    @PrePersist // db에 insert직전 실행. 자동으로
    public void createDate() {
        this.createDate = LocalDate.from(LocalDateTime.now());
    }
}
