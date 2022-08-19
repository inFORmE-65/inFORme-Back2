package com.informe.informeapisb.src.user;

import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.src.user.model.GetScrapRes;
import com.informe.informeapisb.src.user.model.GetUserRes;
import com.informe.informeapisb.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.informe.informeapisb.config.BaseResponseStatus.*;

@Service
public class UserProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;

    private final JwtService jwtService;

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public GetUserRes getUserByEmail(String email) throws BaseException {
        try {
            GetUserRes getUserRes = userDao.getUserByEmail(email);
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkEmail(String email) throws BaseException {
        try {
            return userDao.checkEmail(email);
        } catch(Exception exception) {
            throw new BaseException(EMAIL_CHECK_ERROR);
        }
    }

    public int checkUserExist(int userIdx) throws BaseException {
        try{
            return userDao.checkUserExist(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkNickname(String nickname) throws BaseException {
        try {
            return userDao.checkNicknameExist(nickname);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetScrapRes> scrapList(int userIdx) throws BaseException {
        if(checkUserExist(userIdx) == 0){
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        try {
            List<GetScrapRes> getscraps = userDao.selectScrap(userIdx);

            return getscraps;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
