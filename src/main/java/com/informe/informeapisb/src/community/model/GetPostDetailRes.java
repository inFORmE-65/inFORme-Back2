package com.informe.informeapisb.src.community.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetPostDetailRes {
    private int postIdx;
    private int userIdx;
    private String nickname;
    private String ServiceName;
    private String title;
    private String content;
    private String SVC_ID;
    //총 좋아요 수
    private int postLikeCount;
    //좋아요 누른 유저 닉네임 리스트
    private List<GetPostLikeNick> likeNickList;
    //api 사용 유저가 좋아요 눌렀는지 여부 확인
    private String postLikeCheck;
    private String createdAt;
    private String updatedAt;
    private List<GetImgData> ImgDataList;
    //총 댓글 수
    private int CommentCount;
    private List<GetPostCommentDetail> CommentList;
}
