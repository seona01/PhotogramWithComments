package com.cos.photogramstart.service;

import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.comment.CommentRepository;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public Comment writeComment(String content, int imageId, int userId) {

        // db에서 가져올때 가짜 객체를 만들기 : return시 image객체와 user객체는 id값만 가지고 있는 빈 객체 리턴받음
        Image image = new Image();
        image.setId(imageId);       // id값만 들어가게 되는데 실제 db에 id값만 들어가면 되서
        User userEntity = userRepository.findById(userId).orElseThrow(()->{
           throw new CustomApiException("유저 아이디를 찾을 수 없습니다");               // api호출
        });

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setImage(image);
        comment.setUser(userEntity);

        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(int id) {
        // 나중에 예외처리 하고 싶을 경우 try-catch걸기 throw new CustomApiException(e.getMessage());
        try {
            commentRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomApiException(e.getMessage());
        }
    }
}
