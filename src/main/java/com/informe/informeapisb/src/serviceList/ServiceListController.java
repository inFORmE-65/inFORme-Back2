package com.informe.informeapisb.src.serviceList;

import com.informe.informeapisb.src.serviceList.model.*;
import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.config.BaseResponse;
import com.informe.informeapisb.src.serviceList.model.hits.GetHitsServiceListRes;
import com.informe.informeapisb.src.serviceList.model.recentServiceList.GetRecentServiceInfoRes;
import com.informe.informeapisb.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.informe.informeapisb.config.BaseResponseStatus.*;
import static com.informe.informeapisb.utils.ValidationRegex.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


@RestController
@RequestMapping("/serviceList")
public class ServiceListController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ServiceListProvider serviceListProvider;
    @Autowired
    private final ServiceListService serviceListService;
    @Autowired
    private final JwtService jwtService;

    public ServiceListController(ServiceListProvider serviceListProvider, ServiceListService serviceListService, JwtService jwtService){
        this.serviceListProvider = serviceListProvider;
        this.serviceListService = serviceListService;
        this.jwtService = jwtService;
    }

    //Get 전체 정책 데이터 공공데이터 api 이용
    @ResponseBody
    @GetMapping("/api/{page}/{perPage}")
    public String callApi(@PathVariable("page")int page,@PathVariable("perPage")int perPage) throws IOException {
        StringBuilder result = new StringBuilder();

        String urlStr = "https://api.odcloud.kr/api/gov24/v1/serviceList?" +
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
    public BaseResponse<GetServiceListRes> getServiceList(@PathVariable("page")int page,@PathVariable("perPage")int perPage){
        try {
            GetServiceListRes getServiceListRes = serviceListProvider.getServiceList(page, perPage);
            return new BaseResponse<>(getServiceListRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //Post json 데이터 ServiceList DB 삽입
    @ResponseBody
    @PostMapping("/json")
    public  BaseResponse<String> createServiceList(@RequestBody PostServiceListReq postServiceListReq) throws IOException{
        try{
            if(postServiceListReq.getCurrentCount() == 0){
                String result = "데이터가 없음";
                return new BaseResponse<>(result);
            }
            serviceListService.createServiceList(postServiceListReq.getData().size(),postServiceListReq.getData());
            String result = "성공";
            return new BaseResponse<>(result);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    
    // 실시간 정책 목록 조회 api
    @ResponseBody
    @GetMapping("/hits")
    public BaseResponse<List<GetHitsServiceListRes>> getHitsServiceList(@RequestParam int offset, @RequestParam int limit) {
        try {
            List<GetHitsServiceListRes> getHitsServiceListRes = serviceListProvider.getHitsServiceList(offset, limit);
            return new BaseResponse<>(getHitsServiceListRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    // 최신 정책 조회
    @ResponseBody
    @GetMapping("/recent")
    public BaseResponse<List<GetRecentServiceInfoRes>> getRecentServiceList(@RequestParam int offset, @RequestParam int limit){
        try{
            List<GetRecentServiceInfoRes> getRecentServiceInfoRes = serviceListProvider.getRecentServiceList(offset,limit);
            return new BaseResponse<>(getRecentServiceInfoRes);
        }catch (BaseException exception){
            logger.error("Error!", exception);
            return new BaseResponse<>(exception.getStatus());

        }
    }
}
