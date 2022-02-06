package com.cos.photogramstart.domain.likes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LikesRepository extends JpaRepository<Likes, Integer> {

    @Modifying
    @Query(value = "INSERT INTO Likes(imageId, userId, createDate) VALUES(:imageId, :principalId, now())", nativeQuery = true)
    int mLikes(int imageId, int principalId);

    @Modifying
    @Query(value = "DELETE FROM Likes WHERE imageId = :imageId AND userId = :principalId", nativeQuery = true)
    int mUnlikes(int imageId, int principalId);
}
