package com.informe.informeapisb.src.serviceDetail;

import com.informe.informeapisb.src.serviceDetail.model.*;
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
public class ServiceDetailService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ServiceDetailDao serviceDetailDao;
    private final ServiceDetailProvider serviceDetailProvider;
    private final JwtService jwtService;

    @Autowired
    public ServiceDetailService(ServiceDetailDao serviceDetailDao, ServiceDetailProvider serviceDetailProvider, JwtService jwtService) {
        this.serviceDetailDao = serviceDetailDao;
        this.serviceDetailProvider = serviceDetailProvider;
        this.jwtService = jwtService;
    }
}
