package com.informe.informeapisb.src.supportConditions;


import com.informe.informeapisb.src.supportConditions.model.*;
import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.config.BaseResponse;
import com.informe.informeapisb.utils.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/supportConditions")
public class SupportConditionsController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final SupportConditionsProvider supportConditionsProvider;
    @Autowired
    private final SupportConditionsService supportConditionsService;
    @Autowired
    private final JwtService jwtService;

    private SupportConditionsDao supportConditionsDao;

    public SupportConditionsController(SupportConditionsProvider supportConditionsProvider, SupportConditionsService supportConditionsService, JwtService jwtService){
        this.supportConditionsProvider = supportConditionsProvider;
        this.supportConditionsService = supportConditionsService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @GetMapping("/api/{page}/{perPage}")
    public String callApi(@PathVariable("page")int page, @PathVariable("perPage")int perPage) throws IOException {
        StringBuilder result = new StringBuilder();

        String urlStr = "https://api.odcloud.kr/api/gov24/v1/supportConditions?" +
                "page="+page+"&"+
                "perPage="+perPage+"&"+
                "serviceKey=4qdywegfVpdcSvD0uF1zrGAJ4VMzz9V%2Fybv%2FD6U0NsNY9OpKYNKE8IOqfgyj912iwCHDcmYoFlxNOlND07KsZA%3D%3D";
        URL url = new URL(urlStr);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");

        BufferedReader br;
        br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
        String returnLine;
        while ((returnLine = br.readLine()) != null) {
            result.append(returnLine + "\n\r");
        }

        urlConnection.disconnect();
        return result.toString();
    }

    //Get db 이용 ServiceList 이용
    @ResponseBody
    @GetMapping("/db/{page}/{perPage}")
    public BaseResponse<GetSupportConditionsRes> getSupportConditionsRes(@PathVariable("page")int page, @PathVariable("perPage")int perPage){
        try {
            GetSupportConditionsRes getSupportConditionsRes = supportConditionsProvider.getSupportConditions(page, perPage);
            return new BaseResponse<>(getSupportConditionsRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/getServiceID")
    public List<String> printServiceIds() {
        log.info("실행");
        return supportConditionsDao.getAllServiceId();
    }

    //Post json 데이터 supportDetail DB 삽입
    @ResponseBody
    @PostMapping("/json")
    public  BaseResponse<String> createSupportConditions(@RequestBody PostSupportConditionsReq postSupportConditionsReq) throws IOException{
        try{
            if(postSupportConditionsReq.getCurrentCount() == 0){
                String result = "데이터가 없음";
                return new BaseResponse<>(result);
            }
            supportConditionsService.createSupportConditions(postSupportConditionsReq.getData().size(),postSupportConditionsReq.getData());
            String result = "성공";
            return new BaseResponse<>(result);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 추천 정책 조회 api
    // http://localhost:8080/supportConditions/hits2?offset=1&limit=10&age=10&income_range=100
    // http://localhost:8080/supportConditions/recommend?offset=1&limit=10&age=23&income_range=150
    @ResponseBody
    @GetMapping("/recommend/{page}/{perPage}")
    public BaseResponse<List<GetRecommendSupportConditionsRes>> getRecommendSupportConditionsRes(@PathVariable("page")int page, @PathVariable("perPage")int perPage,
                                                                                                 @RequestParam int age, @RequestParam int income_range, @RequestParam int gender, @RequestParam String area, @RequestParam(value = "personalArray") int[] personalArray,
                                                                                                 @RequestParam(value = "householdslArray") int[] householdslArray){
        try{
            List<GetRecommendSupportConditionsRes> getRecommendSupportConditionsRes = supportConditionsProvider.getRecommendSupportConditions(page, perPage, age, income_range, gender, area, personalArray, householdslArray);
            return new BaseResponse<>(getRecommendSupportConditionsRes);
        }catch (BaseException exception){
            logger.error("Error", exception);
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/recommend2/{userIdx}/{page}/{perPage}")
    public BaseResponse<List<GetRecommendSupportConditionsRes>> getRecommendSupportConditionsRes2(@PathVariable("userIdx")String userIdx, @PathVariable("page")int page, @PathVariable("perPage")int perPage){
        try{
            List<GetProfileData> getProfileData = supportConditionsProvider.getProfile(userIdx);
            GetProfileData age_info= getProfileData.get(0);
            log.info(String.valueOf(age_info));
            int age=23;
            int income_range=100;
            int gender=0;



            List<GetRecommendSupportConditionsRes> getRecommendSupportConditionsRes = supportConditionsProvider.getRecommendSupportConditions2(page, perPage, age, income_range, gender);
            return new BaseResponse<>(getRecommendSupportConditionsRes);
        }catch (BaseException exception){
            logger.error("Error", exception);
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    @ResponseBody
    @GetMapping("/profile/{userIdx}")
    public BaseResponse<List<GetProfileData>> getProfileData(@PathVariable("userIdx") String userIdx){
        try{
            List<GetProfileData> getProfileData = supportConditionsProvider.getProfile(userIdx);
            return new BaseResponse<>(getProfileData);
        }catch (BaseException exception){
            logger.error("Error", exception);
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/profile2/{userIdx}")
    public BaseResponse<List<GetProfileData>> getProfileRes(@PathVariable("userIdx") String userIdx){
        try{
            List<GetProfileData> getProfileData = supportConditionsProvider.getProfile(userIdx);
            return new BaseResponse<>(getProfileData);
        }catch (BaseException exception){
            logger.error("Error", exception);
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    // 수정사항
    /*
    @ResponseBody
    @GetMapping("/recommend2/{page}/{perPage}/{age}/{income_range}")
    public BaseResponse<GetSupportConditionsRes> getSupportConditionsRes(@PathVariable("page")int page, @PathVariable("perPage")int perPage, @PathVariable("age")int age, @PathVariable("income_range")int income_range){
        try {
            GetSupportConditionsRes getSupportConditionsRes = supportConditionsProvider.getSupportConditions2(page, perPage, age, income_range);
            return new BaseResponse<>(getSupportConditionsRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

     */

}
