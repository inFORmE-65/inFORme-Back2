package com.informe.informeapisb.src.community.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostImgData {
    private String origFileName;  // 파일 원본명
    private String fileName; // 파일 이름 변환명
    private String filePath;  // 파일 저장 경로
    private Long fileSize;
}
