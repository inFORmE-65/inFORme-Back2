package com.informe.informeapisb.src.supportConditions;

import com.informe.informeapisb.src.supportConditions.model.*;
import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.informe.informeapisb.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class SupportConditionsProvider {
    private final SupportConditionsDao supportConditionsDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SupportConditionsProvider(SupportConditionsDao supportConditionsDao, JwtService jwtService) {
        this.supportConditionsDao = supportConditionsDao;
        this.jwtService = jwtService;
    }

    public GetSupportConditionsRes getSupportConditions(int page, int perPage) throws BaseException{
        try{
            GetSupportConditionsRes getSupportConditionsRes = supportConditionsDao.getSupportConditions(page, perPage);
            return getSupportConditionsRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 추천 정책 조회
    public List<GetRecommendSupportConditionsRes> getRecommendSupportConditions(int offset, int limit, int age, int income_range) throws BaseException{
        try{
            List<GetRecommendSupportConditionsRes> getRecommendSupportConditionsRes = supportConditionsDao.getRecommendSupportConditions(offset, limit, age, income_range);
            return getRecommendSupportConditionsRes;
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
