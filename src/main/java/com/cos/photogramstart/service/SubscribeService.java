package com.cos.photogramstart.service;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDTO;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;                 // QLRM : db에서 result된 결과를 javaclass에 매핑해주는 라이브러리
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;
    private final EntityManager em;             // 모든 Repository는 EntityManager를 구현해서 만들어져있는 구현체

    @Transactional(readOnly = true)
    public List<SubscribeDTO> slist(int principalId, int pageUserId) {

        StringBuffer sb = new StringBuffer();

        // 쿼리 준비
        sb.append("SELECT u.id, u.username, u.profileImageUrl, ");      // 맨마지막 항상 띄워쓰기
        sb.append("if ((SELECT 1 FROM Subscribe WHERE fromUserId=? AND toUserId=u.id), 1, 0) subscribeState, ");
        sb.append("if ((?=u.id), 1, 0) equalUserState ");
        sb.append("FROM User u INNER JOIN Subscribe s ");
        sb.append("ON u.id = s.toUserId ");
        sb.append("WHERE s.fromUserId = ?");                            // 마지막줄 띄워쓰기,세미콜론 넣지 말기

        // 쿼리 완성
        Query query = em.createNativeQuery(sb.toString())
                .setParameter(1, principalId)
                .setParameter(2, principalId)
                .setParameter(3, pageUserId);

        // 쿼리 실행. 구독리스트 완성 (QLRM 라이브러리 필요 : DTO에 DB결과 매핑하기 위해서)
        JpaResultMapper result = new JpaResultMapper();
        List<SubscribeDTO> subscribeDTOS = result.list(query, SubscribeDTO.class);      // 쿼리 한줄일경우 unique로 받기

        return subscribeDTOS;
    }

    @Transactional      // insert, delete
    public void follow(int fromUserId, int toUserId) {
        try {
            subscribeRepository.mSubscribe(fromUserId, toUserId);
        } catch (Exception e) {
            throw new CustomApiException("이미 구독 중입니다");
        }

    }

    @Transactional
    public void unfollow(int fromUserId, int toUserId) {
        subscribeRepository.mUnsubscribe(fromUserId, toUserId);
    }
    // 구독 취소는 오류 케이스 별로 없으므로
}
