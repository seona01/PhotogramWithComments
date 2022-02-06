package com.cos.photogramstart.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CMRespDTO<T> {             // 제네릭 처리
    private int code;                   // 1:성공 -1:실패
    private String message;
    private T data;
}
