package com.cos.photogramstart.domain.subscribe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubscribeRepository extends JpaRepository<Subscribe, Integer> {

    // 네이티브 쿼리 만들기
    @Modifying  // insert, update, delete를 네이티브 쿼리로 작성하려면 @ 필요
    @Query(value = "INSERT INTO Subscribe(fromUserId, toUserId, createDate) VALUES (:fromUserId, :toUserId, now())", nativeQuery = true)
    void mSubscribe(int fromUserId, int toUserId);
    // int형으로 받을 경우 성공1, -1. 10행 성공하면 10 리턴

    @Modifying
    @Query(value = "DELETE FROM Subscribe WHERE fromUserId = :fromUserId AND toUserId = :toUserId", nativeQuery = true)
    void mUnsubscribe(int fromUserId, int toUserId);

    @Query(value="SELECT COUNT(*) FROM Subscribe WHERE fromUserId = :principalId AND toUserId = :pageUserId", nativeQuery = true)
    int mSubscribeState(int principalId, int pageUserId);

    @Query(value="SELECT COUNT(*) FROM Subscribe WHERE fromUserId = :pageUserId", nativeQuery = true)
    int mSubscribeCount(int pageUserId);
}
