package com.informe.informeapisb.src.serviceList;

import com.informe.informeapisb.src.serviceList.model.*;
import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.config.BaseResponse;
import com.informe.informeapisb.src.serviceList.model.recentServiceList.GetRecentServiceInfoRes;
import com.informe.informeapisb.src.serviceList.model.recentServiceList.GetRecentServiceListRes;
import com.informe.informeapisb.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.informe.informeapisb.config.BaseResponseStatus.*;
import static com.informe.informeapisb.utils.ValidationRegex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.informe.informeapisb.config.BaseResponseStatus.DATABASE_ERROR;
import static com.informe.informeapisb.config.BaseResponseStatus.USERS_EMPTY_USER_ID;

@Service
public class ServiceListProvider {
    private final ServiceListDao serviceListDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ServiceListProvider(ServiceListDao serviceListDao, JwtService jwtService) {
        this.serviceListDao = serviceListDao;
        this.jwtService = jwtService;
    }

    public GetServiceListRes getServiceList(int page, int perPage) throws BaseException{
        try{
            GetServiceListRes getServiceListRes = serviceListDao.getServiceList(page, perPage);
            return getServiceListRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 최신 정책 조회
    public GetRecentServiceListRes getRecentServiceList() throws BaseException{
        try{
            List<GetRecentServiceInfoRes> getRecentServiceInfoRes = serviceListDao.getRecentServiceInfoRes();
            GetRecentServiceListRes getRecentServiceListRes = new GetRecentServiceListRes(getRecentServiceInfoRes);
            return getRecentServiceListRes;
        }
        catch (Exception exception){
            logger.error("Error!", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
