package com.informe.informeapisb.src.community;

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

}
