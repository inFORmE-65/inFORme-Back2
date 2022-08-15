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
    public List<GetPostsRes> getPosts(String type, int userIdx) throws BaseException {

        try{
            List<GetPostsRes> getPostsRes = communityDao.getPosts(type, userIdx);
            return getPostsRes;

        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    //단일 게시물 조회
    public GetPostsRes getPost(int postIdx, int userIdx) throws BaseException {

        try{
            GetPostsRes getPostsRes = communityDao.getPost(postIdx, userIdx);
            return getPostsRes;

        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    //단일 게시물 세부사항 조회
    public GetPostDetailRes getPostDetail(int postIdx, int userIdx) throws BaseException {

        try{
            GetPostDetailRes getPostDetailRes = communityDao.getPostDetail(postIdx, userIdx);
            return getPostDetailRes;

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

    //좋아요 활성화 확인
    @Transactional(readOnly = true)
    public int checkLikeActiveExist(int userIdx,int postIdx) throws BaseException {
        try {
            return communityDao.checkLikeActiveExist(userIdx, postIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //좋아요 비활성화 확인
    @Transactional(readOnly = true)
    public int checkLikeInActiveExist(int userIdx,int postIdx) throws BaseException {
        try {
            return communityDao.checkLikeInActiveExist(userIdx, postIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //댓글 commentIdx로 조회
    @Transactional(readOnly = true)
    public GetPostComment getCommentByIdx(int commentIdx) throws BaseException {
        try {
            GetPostComment getPostComment = communityDao.getCommentByIdx(commentIdx);
            return getPostComment;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
