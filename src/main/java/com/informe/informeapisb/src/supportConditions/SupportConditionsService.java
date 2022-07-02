package com.informe.informeapisb.src.supportConditions;

import com.informe.informeapisb.src.supportConditions.model.*;
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

import static com.informe.informeapisb.config.BaseResponseStatus.*;

@Service
public class SupportConditionsService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SupportConditionsDao supportConditionsDao;
    private final SupportConditionsProvider supportConditionsProvider;
    private final JwtService jwtService;

    @Autowired
    public SupportConditionsService(SupportConditionsDao supportConditionsDao, SupportConditionsProvider supportConditionsProvider, JwtService jwtService) {
        this.supportConditionsDao = supportConditionsDao;
        this.supportConditionsProvider = supportConditionsProvider;
        this.jwtService = jwtService;
    }
}
