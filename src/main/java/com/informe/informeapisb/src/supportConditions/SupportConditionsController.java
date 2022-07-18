package com.informe.informeapisb.src.supportConditions;


import com.informe.informeapisb.src.supportConditions.model.*;
import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.config.BaseResponse;
import com.informe.informeapisb.utils.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.informe.informeapisb.config.BaseResponseStatus.*;
import static com.informe.informeapisb.utils.ValidationRegex.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

    @GetMapping("/getServiceID")
    public List<String> printServiceIds() {
        log.info("실행");
        return supportConditionsDao.getAllServiceId();
    }

    /*
    @RequestMapping("/test1")
    public String index(Model model) {
        // 맵퍼로부터 리스트를 받아옴.
        List<SupportConditionsData> books = SupportConditionsMapper.getList();
        // 모델을 통해 뷰페이지로 데이터를 전달
        model.addAttribute("books", books);
        return "books/index";
    }
     */

    /*
    @RequestMapping(value = "/test2")
    public ModelAndView test() throws Exception{
        ModelAndView mav = new ModelAndView("test");

        List<SupportConditionsData> testList = supportConditionsService.getConditionsList();
        mav.addObject("list", testList);

        return mav;
    }

     */


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
    @GetMapping("/recommend")
    public BaseResponse<List<GetRecommendSupportConditionsRes>> getRecommendSupportConditions(@RequestParam int offset, @RequestParam int limit, @RequestParam int age, @RequestParam int incomeRange){
        try{
            List<GetRecommendSupportConditionsRes> getRecommendSupportConditionsRes = supportConditionsProvider.getRecommendSupportConditions(offset, limit, age, incomeRange);
            return new BaseResponse<>(getRecommendSupportConditionsRes);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/api2")
    public String callApi2() throws IOException {
        StringBuilder result = new StringBuilder();

        result.append("one");
        log.info("성공");
        return result.toString();
    }
}
