package com.informe.informeapisb.src.community.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPostCommentDetail {
    private int commentIdx;
    private int userIdx;
    private int postIdx;
    private String nickname;
    private String content;
    private String status;
    private String createdAt;
    private String updatedAt;
    private List<GetPostComment> childCommentList;
}
