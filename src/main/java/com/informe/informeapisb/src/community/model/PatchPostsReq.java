package com.informe.informeapisb.src.community.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PatchPostsReq {
    private int postIdx;
    private int userIdx;
    private String title;
    private String content;
    private String SVC_ID;
}
