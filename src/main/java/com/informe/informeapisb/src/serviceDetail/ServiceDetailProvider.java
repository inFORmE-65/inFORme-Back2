package com.informe.informeapisb.src.serviceDetail;

import com.informe.informeapisb.src.serviceDetail.model.*;
import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.config.BaseResponse;
import com.informe.informeapisb.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.informe.informeapisb.config.BaseResponseStatus.*;
import static com.informe.informeapisb.utils.ValidationRegex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.informe.informeapisb.config.BaseResponseStatus.DATABASE_ERROR;
import static com.informe.informeapisb.config.BaseResponseStatus.USERS_EMPTY_USER_ID;

@Service
public class ServiceDetailProvider {
    private final ServiceDetailDao serviceDetailDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ServiceDetailProvider(ServiceDetailDao serviceDetailDao, JwtService jwtService) {
        this.serviceDetailDao = serviceDetailDao;
        this.jwtService = jwtService;
    }

    public GetServiceDetailBySVCIDRes getServiceDetailBySVCID(String SVC_ID) throws BaseException{
        try{
            GetServiceDetailBySVCIDRes getServiceDetailRes = serviceDetailDao.getServiceDetailBySVCID(SVC_ID);
            return getServiceDetailRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetSVCIDByServiceNameRes getSVCIDByServiceName(String ServiceName) throws BaseException{
        try{
            GetSVCIDByServiceNameRes getSVC_IDRes = serviceDetailDao.getSVCIDByServiceName(ServiceName);
            return getSVC_IDRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
