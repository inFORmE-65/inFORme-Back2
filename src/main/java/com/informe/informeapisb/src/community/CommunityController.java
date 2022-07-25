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
    public BaseResponse<List<GetPostsRes>> getPost(@PathVariable("type") String type ) {
        try{
            if(!(type.equals("Free")||type.equals("Policy"))){
                return new BaseResponse<>(GET_POST_PATH_ERROR);
            }
            List<GetPostsRes> getPostsRes = communityProvider.getPost(type);
            return new BaseResponse<>(getPostsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
