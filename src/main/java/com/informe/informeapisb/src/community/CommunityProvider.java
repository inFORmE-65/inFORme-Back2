package com.informe.informeapisb.src.community;

import com.informe.informeapisb.src.community.model.*;
import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.config.BaseResponse;
import com.informe.informeapisb.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.informe.informeapisb.config.BaseResponseStatus.*;
import static com.informe.informeapisb.utils.ValidationRegex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.informe.informeapisb.config.BaseResponseStatus.DATABASE_ERROR;
import static com.informe.informeapisb.config.BaseResponseStatus.USERS_EMPTY_USER_ID;

@Service
public class CommunityProvider {
    private final CommunityDao communityDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CommunityProvider(CommunityDao communityDao, JwtService jwtService) {
        this.communityDao = communityDao;
        this.jwtService = jwtService;
    }

    @Transactional(readOnly = true)
    //게시물 리스트 조회
    public List<GetPostsRes> getPosts(String type) throws BaseException {

        try{
            List<GetPostsRes> getPostsRes = communityDao.getPosts(type);
            return getPostsRes;

        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    //단일 게시물 조회
    public GetPostsRes getPost(int postIdx) throws BaseException {

        try{
            GetPostsRes getPostsRes = communityDao.getPost(postIdx);
            return getPostsRes;

        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    //특정 유저의 게시물 리스트 조회
    public List<GetPostsRes> getUserPosts(int userIdx) throws BaseException {

        try{
            List<GetPostsRes> getPostsRes = communityDao.getUserPosts(userIdx);
            return getPostsRes;

        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
