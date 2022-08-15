package com.informe.informeapisb.src.community.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPostComment {
    private int commentIdx;
    private int userIdx;
    private int postIdx;
    private String nickname;
    private String content;
    private int parentCommentIdx;
    private String status;
    private String createdAt;
    private String updatedAt;
}
