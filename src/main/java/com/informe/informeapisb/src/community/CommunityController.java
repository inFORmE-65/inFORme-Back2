package com.informe.informeapisb.src.community;

import com.informe.informeapisb.config.BaseResponseStatus;
import com.informe.informeapisb.src.community.model.*;
import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.config.BaseResponse;
import com.informe.informeapisb.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    public CommunityController(CommunityProvider communityProvider, CommunityService communityService, JwtService jwtService){
        this.communityProvider = communityProvider;
        this.communityService = communityService;
        this.jwtService = jwtService;
    }

    //게시물 생성 api
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostPostsRes> createPost(@RequestBody PostPostsReq postPostsReq) {
        try{
            //제목 길이 확인
            if(postPostsReq.getTitle().length()<2){
                return new BaseResponse<>(POST_POSTS_EMPTY_TITLE);
            }
            PostPostsRes postPostsRes = communityService.createPost(postPostsReq.getUserIdx(), postPostsReq);
            return new BaseResponse<>(postPostsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //최신 게시물 조회 api (Free or Policy)
    @ResponseBody
    @GetMapping("/{type}")
    public BaseResponse<List<GetPostsRes>> getPosts(@PathVariable("type") String type ) {
        try{
            if(!(type.equals("Free")||type.equals("Policy"))){
                return new BaseResponse<>(GET_POST_PATH_ERROR);
            }
            List<GetPostsRes> getPostsRes = communityProvider.getPosts(type);
            return new BaseResponse<>(getPostsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    //특정 단일 게시물 조회 api (해당 게시물의 내용)
    @ResponseBody
    @GetMapping("/post")
    public BaseResponse<GetPostsRes> getPost(@RequestParam int postIdx) {
        try{
            GetPostsRes getPostsRes = communityProvider.getPost(postIdx);
            return new BaseResponse<>(getPostsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //특정 유저의 게시물 리스트 조회 api (해당 게시물의 내용)
    @ResponseBody
    @GetMapping("/user")
    public BaseResponse<List<GetPostsRes>> getUserPosts(@RequestParam int userIdx) {
        try{
            List<GetPostsRes> getPostsRes = communityProvider.getUserPosts(userIdx);
            return new BaseResponse<>(getPostsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    //게시물 수정 api (사진 삭제, 추가는 아직 안 됨) 사진은 변경만 가능, 마이페이지 내가 쓴 글에서 게시글 수정가능
    //QueryString
    @ResponseBody
    @PatchMapping("")
    public BaseResponse<String> modifyPost(@RequestParam int userIdx,@RequestParam int postIdx,@RequestBody PatchPostsReq patchPostsReq) {
        try{
            //수정할 제목 길이 확인
            if(patchPostsReq.getTitle().length()<2){
                return new BaseResponse<>(POST_POSTS_EMPTY_TITLE);
            }

            communityService.modifyPost(userIdx,postIdx,patchPostsReq);
            String result = "게시물 내용을 수정하였습니다.";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
