package com.cos.photogramstart.web.dto.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommentDTO {
    @NotBlank       // 빈값, 공백 또는 null 처리
    private String content;
    @NotNull       // null 처리
    private Integer imageId;

    // toEntity 필요없음
}
/*
* NotNull : null값 체크
* NotEmpty : 빈값 또는 null체크
* NotBlank : 빈값, 공백 또는 null체크
* */