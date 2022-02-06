package com.cos.photogramstart.domain.image;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Integer> {

    @Query(value = "SELECT * FROM Image WHERE userId IN (SELECT toUserId FROM Subscribe WHERE fromUserId = :principalId) ORDER BY id DESC", nativeQuery = true)
    Page<Image> mStory(int principalId, Pageable pageable);

    // 가독성 상 서브쿼리로 구현
    @Query(value = "SELECT i.* FROM Image i INNER JOIN (SELECT imageId, COUNT(imageId) likeCount FROM Likes GROUP BY imageId) c ON i.id = c.imageId ORDER BY likeCount DESC", nativeQuery = true)
    List<Image> mPopular();
}
