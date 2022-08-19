package com.informe.informeapisb.src.user;

import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.src.user.model.*;
import com.informe.informeapisb.utils.JwtService;
import com.informe.informeapisb.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.informe.informeapisb.config.BaseResponseStatus.*;

@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;

    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;
    }

    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        // 이메일 중복 확인
        if(userProvider.checkEmail(postUserReq.getEmail()) == 1) {
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
        // 닉네임 중복 확인
        if(userProvider.checkNickname(postUserReq.getNickname()) == 1){
            throw new BaseException(POST_USERS_EXISTS_NICKNAME);
        }

        // 데이터 암호화
        String pwd;
        try {
            pwd = new SHA256().encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pwd);
        } catch(Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        try {
            int userIdx = userDao.createUser(postUserReq);
            userDao.setProfile(userIdx, postUserReq.getBirth());

            // jwt 생성
            try {
                String jwt = jwtService.createJwt(userIdx);
                return new PostUserRes(jwt, userIdx);
            } catch (Exception exception) {
                throw new BaseException(JWT_ERROR);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyProfile(int userIdx, PostProfileReq postProfileReq) throws BaseException {
        if(userProvider.checkUserExist(userIdx) ==0){
            throw new BaseException(USERS_EMPTY_USER_ID);
        }
        try{
            int result = userDao.updateProfile(userIdx,postProfileReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteUser(int userIdx) throws BaseException {
        if(userProvider.checkUserExist(userIdx) == 0){
            throw new BaseException(USERS_EMPTY_USER_ID);
        }
        try{
            int result = userDao.updateUserStatus(userIdx);

            if(result == 0){
                throw new BaseException(DELETE_FAIL_USER);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void scrapService(int userIdx, PostScrapReq postScrapReq) throws BaseException {
        if(userProvider.checkUserExist(userIdx) == 0) {
            throw new BaseException(USERS_EMPTY_USER_ID);
        }
        System.out.println(postScrapReq.getSVC_ID());
        try {
            int scrapIdx = userDao.scrapService(userIdx, postScrapReq);
            System.out.print(scrapIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public void deleteScrap(int userIdx, PostScrapReq postScrapReq) throws BaseException {
        if (userProvider.checkUserExist(userIdx) == 0) {
            throw new BaseException(USERS_EMPTY_USER_ID);
        }
        try {
            int result = userDao.deleteScrap(userIdx, postScrapReq);

            if (result == 0) {
                throw new BaseException(DELETE_FAIL_SCRAP);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
