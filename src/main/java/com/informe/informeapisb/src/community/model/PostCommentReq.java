package com.informe.informeapisb.src.community.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostCommentReq {
    private String content;
    private int parentCommentIdx;
}
