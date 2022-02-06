package com.cos.photogramstart.web.api;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.service.CommentService;
import com.cos.photogramstart.web.dto.CMRespDTO;
import com.cos.photogramstart.web.dto.comment.CommentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class CommentApiController {                     // ajax로 전송해서 ApiController

    private final CommentService commentService;

    @PostMapping("/api/comment")
    public ResponseEntity<?> commentSave(@Valid @RequestBody CommentDTO commentDTO, BindingResult bindingResult, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        // @RequestBody 붙여야 JSON형태 받을 수 있고, 붙이지 않으면 xxx urlencoded를 받는다
        // @Valid 걸고 바로 다음에 BindingResult걸기
        // System.out.println(commentDTO);

        Comment comment = commentService.writeComment(commentDTO.getContent(), commentDTO.getImageId(), principalDetails.getUser().getId());
        return new ResponseEntity<>(new CMRespDTO<>(1, "댓글쓰기성공", comment), HttpStatus.CREATED);
    }

    @DeleteMapping("/api/comment/{id}")
    public ResponseEntity<?> commentDelete(@PathVariable int id) {
        commentService.deleteComment(id);
        return new ResponseEntity<>(new CMRespDTO<>(1, "댓글삭제성공", null), HttpStatus.OK);
    }

}
