package com.informe.informeapisb.src.auth;

import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.src.auth.model.*;
import com.informe.informeapisb.utils.JwtService;
import com.informe.informeapisb.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.informe.informeapisb.config.BaseResponseStatus.*;

@Service
public class AuthService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AuthDao authDao;
    private final AuthProvider authProvider;
    private final JwtService jwtService;


    @Autowired
    public AuthService(AuthDao authDao, AuthProvider authProvider, JwtService jwtService) {
        this.authDao = authDao;
        this.authProvider = authProvider;
        this.jwtService = jwtService;
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException {
        User user = authDao.getPwd(postLoginReq);
        String encrypPwd;

        try {
            encrypPwd = new SHA256().encrypt(postLoginReq.getPassword());
        } catch (Exception exception) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        if(user.getPassword().equals(encrypPwd)) {
            int userIdx = user.getUserIdx();
            String jwt = jwtService.createJwt(userIdx);

            return new PostLoginRes(userIdx, jwt);
        } else {
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

}
