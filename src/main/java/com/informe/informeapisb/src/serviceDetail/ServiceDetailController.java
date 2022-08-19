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
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/serviceDetail")
public class ServiceDetailController {


    @Autowired
    private final ServiceDetailProvider serviceDetailProvider;
    @Autowired
    private final ServiceDetailService serviceDetailService;
    @Autowired
    private final JwtService jwtService;

    public ServiceDetailController(ServiceDetailProvider serviceDetailProvider, ServiceDetailService serviceDetailService, JwtService jwtService){
        this.serviceDetailProvider = serviceDetailProvider;
        this.serviceDetailService = serviceDetailService;
        this.jwtService = jwtService;
    }

    //Get 전체 정책 데이터 공공데이터 api 이용
    @ResponseBody
    @GetMapping("/api/{page}/{perPage}")
    public String callApi(@PathVariable("page")int page, @PathVariable("perPage")int perPage) throws IOException {
        StringBuilder result = new StringBuilder();

        String urlStr = "https://api.odcloud.kr/api/gov24/v1/serviceDetail?" +
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

    //Post json 데이터 ServiceDetail DB 삽입
    @ResponseBody
    @PostMapping("/json")
    public  BaseResponse<String> createServiceDetail(@RequestBody PostServiceDetailReq postServiceDetailReq) throws IOException{
        try{
            if(postServiceDetailReq.getCurrentCount() == 0){
                String result = "데이터가 없음";
                return new BaseResponse<>(result);
            }
            serviceDetailService.createServiceDetail(postServiceDetailReq.getData().size(),postServiceDetailReq.getData());
            String result = "성공";
            return new BaseResponse<>(result);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //Get DB 이용 ServiceDetail by SVC_ID
    @ResponseBody
    @GetMapping("/SVC_ID")
    public BaseResponse<GetServiceDetailBySVCIDRes> getServiceDetailBySVCID(@RequestParam String SVC_ID){
        try {
            GetServiceDetailBySVCIDRes getServiceDetailRes = serviceDetailProvider.getServiceDetailBySVCID(SVC_ID);
            return new BaseResponse<>(getServiceDetailRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    //Get DB 이용 SVC_ID by ServiceName
    @ResponseBody
    @GetMapping("/SVC_NAME")
    public BaseResponse<GetSVCIDByServiceNameRes> getSVCIDByServiceName(@RequestParam String ServiceName){
        try {
            GetSVCIDByServiceNameRes getSVC_IDRes = serviceDetailProvider.getSVCIDByServiceName(ServiceName);
            return new BaseResponse<>(getSVC_IDRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}