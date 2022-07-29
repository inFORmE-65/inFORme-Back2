package com.informe.informeapisb.src.community;

import com.informe.informeapisb.config.BaseResponse;
import com.informe.informeapisb.src.community.model.*;
import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.informe.informeapisb.config.BaseResponseStatus.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CommunityDao communityDao;
    private final CommunityProvider communityProvider;
    private final JwtService jwtService;

    @Autowired
    public CommunityService(CommunityDao communityDao, CommunityProvider communityProvider, JwtService jwtService) {
        this.communityDao = communityDao;
        this.communityProvider = communityProvider;
        this.jwtService = jwtService;
    }

    public PostPostsRes createPost(int userIdx, PostPostsReq postPostsReq) throws BaseException {
        try{
            int postIdx = communityDao.insertPolicyPost(userIdx, postPostsReq.getTitle(), postPostsReq.getContent(), postPostsReq.getSVC_ID());
            //이미지 삽입(이미지 없을 시 삽입 안함)
            if(postPostsReq.getImgUrls().isEmpty()){
                return new PostPostsRes(postIdx);
            }
            for (int i = 0; i< postPostsReq.getImgUrls().size(); i++){
                communityDao.insertPostImg(postIdx,userIdx,postPostsReq.getImgUrls().get(i));
            }
            return new PostPostsRes(postIdx);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //게시물 수정
    public void modifyPost(int userIdx, int postIdx, PatchPostsReq patchPostsReq) throws BaseException {
        //수정하고자 하는 게시물에서 해당 게시물의 postIdx 를 인자로 받음
        //현재 수정을 원하는 user 의 userIdx 와 수정하고자하는 postIdx , 수정 내용 PatchPostsReq 를 인자로 받음
        //해당하는 postIdx 의 원래의 게시글을 불러옴
        GetPostsRes temp = communityProvider.getPost(postIdx);

        //원본 게시글의 작성자 Idx 와 현재 userIdx 와 비교를 통해 수정 가능한 게시물인지 검증
        // 과 인자로 받은 userIdx, postIdx 비교를 통해 수정 가능한 게시글인지 검증
        if(userIdx != temp.getUserIdx()){
            throw new BaseException(PATCH_POST_AUTH_ERROR);
        }

        //게시글 내용 같을 때 condition1 = True
        boolean condition1 = patchPostsReq.getTitle().equals(temp.getTitle()) && patchPostsReq.getContent().equals(temp.getContent()) && patchPostsReq.getSVC_ID().equals(temp.getSVC_ID());
        int condition2 = 0;

        //사진이 같은 내용이면 업데이트 건너뜀, 수정하는 imgUrl 의 Idx 가 다르다면 Update 안하고 건너뜀
        for(int i=0; i<patchPostsReq.getImgUrls().size();i++){
            if(temp.getImgUrls().get(i).getImgUrl().equals(patchPostsReq.getImgUrls().get(i).getImgUrl())){
                continue;
            } else if (temp.getImgUrls().get(i).getPostImgIdx() != patchPostsReq.getImgUrls().get(i).getPostImgIdx()) {
                continue;
            } else {
                condition2++;
                communityDao.updateImg(patchPostsReq.getImgUrls().get(i));
            }
        }

        //사진을 한 번이라도 업데이트 했거나, 원본 게시글과 내용이 다르면 내용을 업데이트한다
        if((condition2>0) || (!condition1)){
            int result = communityDao.updatePost(postIdx, patchPostsReq);
            if(result==0){
                throw new BaseException(MODIFY_FAIL_POST);
            }
        }else{
            throw new BaseException(MODIFY_FAIL_POST_2);
        }
    }

}
