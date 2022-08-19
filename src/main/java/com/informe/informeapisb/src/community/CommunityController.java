package com.informe.informeapisb.src.community;

import com.amazonaws.services.s3.AmazonS3Client;
import com.informe.informeapisb.src.community.model.*;
import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.config.BaseResponse;
import com.informe.informeapisb.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.informe.informeapisb.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/community")
public class CommunityController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CommunityProvider communityProvider;
    @Autowired
    private final CommunityService communityService;
    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final AmazonS3Client amazonS3Client;

    public CommunityController(CommunityProvider communityProvider, CommunityService communityService, JwtService jwtService, AmazonS3Client amazonS3Client){
        this.communityProvider = communityProvider;
        this.communityService = communityService;
        this.jwtService = jwtService;
        this.amazonS3Client = amazonS3Client;
    }

    //게시물 생성 api
    @ResponseBody
    @PostMapping("/post")
    public BaseResponse<PostPostsRes> createPost(@RequestPart(value="image", required=false) List<MultipartFile> files, @RequestPart(value="postPostsReq") PostPostsReq postPostsReq) {
        try{
            //제목 길이 확인
            if(postPostsReq.getTitle().length()<2){
                return new BaseResponse<>(POST_POSTS_EMPTY_TITLE);
            }

            //파일 확장자 검사
            if(files.get(0).isEmpty() != true){
                for(int i=0; i<files.size();i++){
                    if( files.get(i).getContentType().equals("image/png") || files.get(i).getContentType().equals("image/jpeg")){
                        continue;
                    }
                    else {
                        throw new BaseException(POST_POST_NOT_JPG_PNG);
                    }
                }
            }

            PostPostsRes postPostsRes = communityService.createPost(postPostsReq.getUserIdx(), postPostsReq, files);
            return new BaseResponse<>(postPostsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //최신 게시물 조회 api (Free or Policy)
    @ResponseBody
    @GetMapping("/posts/{type}")
    public BaseResponse<List<GetPostsRes>> getPosts(@PathVariable("type") String type, @RequestParam int userIdx) {
        //api 이용자의 userIdx 제공 받음
        try {
            if (!(type.equals("Free") || type.equals("Policy"))) {
                return new BaseResponse<>(GET_POST_PATH_ERROR);
            }
            List<GetPostsRes> getPostsRes = communityProvider.getPosts(type,userIdx);
            return new BaseResponse<>(getPostsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    //특정 단일 게시물 조회 api (해당 게시물의 세부 내용)
    @ResponseBody
    @GetMapping("/post/post")
    public BaseResponse<GetPostDetailRes> getPost(@RequestParam int postIdx, @RequestParam int userIdx) {
        //api 이용자의 userIdx 제공 받음
        try {
            GetPostDetailRes getPostDetailRes = communityProvider.getPostDetail(postIdx, userIdx);
            return new BaseResponse<>(getPostDetailRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //특정 유저의 게시물 리스트 조회 api (해당 게시물의 내용) (본인이 쓴 글 확인용)
    @ResponseBody
    @GetMapping("/post/user")
    public BaseResponse<List<GetPostsRes>> getUserPosts(@RequestParam int userIdx) {
        try{
            List<GetPostsRes> getPostsRes = communityProvider.getUserPosts(userIdx);
            return new BaseResponse<>(getPostsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    //게시물 수정 api  마이페이지 내가 쓴 글에서 게시글 수정가능
    //QueryString
    @ResponseBody
    @PatchMapping("/post")
    public BaseResponse<String> modifyPost(@RequestPart(value="patchPostsReq") PatchPostsReq patchPostsReq,@RequestPart(value="image", required=false) List<MultipartFile> files) {
        try{
            //수정할 제목 길이 확인
            if(patchPostsReq.getTitle().length()<2){
                return new BaseResponse<>(POST_POSTS_EMPTY_TITLE);
            }

            communityService.modifyPost(patchPostsReq,files);
            String result = "게시물 내용을 수정하였습니다.";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }



    //게시글 좋아요 api
    @ResponseBody
    @PostMapping("/post/like")
    public BaseResponse<String> createLike(@RequestParam int userIdx, @RequestParam int postIdx) {
        try{
            //userIdx는 현재 사용자, postIdx는 좋아요 대상 게시물
            communityService.createLike(userIdx,postIdx);
            String result = "좋아요";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    //게시글 좋아요 취소 api
    @ResponseBody
    @PatchMapping("/post/liked")
    public BaseResponse<String> cancelLike(@RequestParam int userIdx, @RequestParam int postIdx) {
        try{
            communityService.cancelLike(userIdx,postIdx);
            String result = "좋아요 취소";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //게시글 댓글 생성 api
    @ResponseBody
    @PostMapping("/post/comment")
    public BaseResponse<PostCommentRes> insertComment(@RequestParam int userIdx, @RequestParam int postIdx, @RequestBody PostCommentReq postCommentReq) {
        try{
            //userIdx는 현재 사용자, postIdx는 좋아요 대상 게시물
            PostCommentRes postCommentRes = communityService.insertComment(userIdx,postIdx,postCommentReq);
            return new BaseResponse<>(postCommentRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //게시글 댓글 삭제 api
    @ResponseBody
    @PatchMapping("/post/commented")
    public BaseResponse<String> cancelComment(@RequestParam int userIdx, @RequestParam int commentIdx) {
        try{
            communityService.cancelComment(userIdx,commentIdx);
            String result = "댓글 취소";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
