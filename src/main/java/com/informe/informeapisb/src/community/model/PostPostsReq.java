package com.informe.informeapisb.src.community.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostPostsReq {
    private int userIdx;
    private String title;
    private String content;
    private String SVC_ID;
    private List<PostImgUrl> ImgUrls;
}
