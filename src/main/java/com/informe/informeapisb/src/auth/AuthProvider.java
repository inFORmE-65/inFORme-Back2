package com.informe.informeapisb.src.auth;

import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.config.BaseResponseStatus.*;
import com.informe.informeapisb.config.secret.Secret;
import com.informe.informeapisb.src.auth.model.*;
import com.informe.informeapisb.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.informe.informeapisb.config.BaseResponseStatus.EMAIL_CHECK_ERROR;
import static com.informe.informeapisb.config.BaseResponseStatus.FAILED_TO_LOGIN;
import static org.apache.tomcat.util.net.openssl.ciphers.Encryption.AES128;

@Service
public class AuthProvider {

    private final AuthDao authDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public AuthProvider(AuthDao authDao, JwtService jwtService) {
        this.authDao = authDao;
        this.jwtService = jwtService;
    }

    public int checkEmail(String email) throws BaseException {
        try {
            return authDao.checkEmail(email);
        } catch(Exception exception) {
            throw new BaseException(EMAIL_CHECK_ERROR);
        }
    }
}
