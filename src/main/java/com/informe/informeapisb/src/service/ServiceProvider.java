package com.informe.informeapisb.src.service;

import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.src.service.model.GetSearchInfoRes;
import com.informe.informeapisb.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.informe.informeapisb.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ServiceProvider {

    private final ServiceDao serviceDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ServiceProvider(ServiceDao serviceDao, JwtService jwtService) {
        this.serviceDao = serviceDao;
        this.jwtService = jwtService;
    }

    // 정책 검색
    public List<GetSearchInfoRes> getSearchInfoResList(String word) throws BaseException{
        try{
            List<GetSearchInfoRes> getSearchInfoRes = serviceDao.getSearchInfoResList(word);
            return getSearchInfoRes;

        }catch (Exception exception){
            logger.error("Error!", exception);

            throw new BaseException(DATABASE_ERROR);
        }
    }
}
