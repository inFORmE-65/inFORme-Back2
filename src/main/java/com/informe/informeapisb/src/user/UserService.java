package com.informe.informeapisb.src.user;

import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.src.user.model.PostUserReq;
import com.informe.informeapisb.src.user.model.PostUserRes;
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

        // 데이터 암호화
        String pwd;
        try {
            pwd = new SHA256().encrypt(postUserReq.getPassword());
            postUserReq.setPassword(pwd);
        } catch(Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        try {
            // 여기서 계속 데이터베이스 연결 실패함. (내 계정 DB, umchwan DB 둘다 마찬가지)
            // jwtservice 부분을 지워봐도 결과가 같은걸 보면 userDao 문제인듯
            int userIdx = userDao.createUser(postUserReq);

            // jwt은 잘 생성됨.
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
}
