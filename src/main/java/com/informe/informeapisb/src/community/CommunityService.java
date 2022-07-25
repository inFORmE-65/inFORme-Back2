package com.informe.informeapisb.src.community;

import com.informe.informeapisb.src.community.model.*;
import com.informe.informeapisb.config.secret.Secret;
import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.config.BaseResponse;
import com.informe.informeapisb.utils.JwtService;
import com.informe.informeapisb.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.informe.informeapisb.config.BaseResponseStatus.*;
import static com.informe.informeapisb.utils.ValidationRegex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.informe.informeapisb.config.BaseResponseStatus.*;

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
}
