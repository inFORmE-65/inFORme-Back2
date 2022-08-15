package com.informe.informeapisb.src.community.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetImgData {
    private int postImgIdx;
    private String origFileName;
    private String fileName;
    private String filePath;
    private Long fileSize;
}
