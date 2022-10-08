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
    public List<GetRecommendSupportConditionsRes> getRecommendSupportConditions(int page, int perPage, int age, int income_range, int gender, String area, int[] personalArray, int[] householdslArray) throws BaseException{
        try{
            List<GetRecommendSupportConditionsRes> getRecommendSupportConditionsRes = supportConditionsDao.getRecommendSupportConditions(page, perPage, age, income_range, gender, area, personalArray, householdslArray);
            return getRecommendSupportConditionsRes;
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetRecommendSupportConditionsRes> getRecommendSupportConditions2(int page, int perPage, int age, int income_range, int gender) throws BaseException{
        try{
            List<GetRecommendSupportConditionsRes> getRecommendSupportConditionsRes = supportConditionsDao.getRecommendSupportConditions2(page, perPage, age, income_range, gender);
            return getRecommendSupportConditionsRes;
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetProfileData> getProfile(String userIndex) throws BaseException{
        try{
            List<GetProfileData> getProfileRes = supportConditionsDao.getProfile(userIndex);
            return getProfileRes;
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /*
    public GetSupportConditionsRes getSupportConditions2(int page, int perPage, int age, int income_range) throws BaseException{
        try{
            GetSupportConditionsRes getSupportConditionsRes = supportConditionsDao.getSupportConditions2(page, perPage, age, income_range);
            return getSupportConditionsRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

     */

}
