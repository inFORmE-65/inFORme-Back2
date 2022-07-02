package com.informe.informeapisb.src.serviceDetail;

import com.informe.informeapisb.src.serviceDetail.model.*;
import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.config.BaseResponse;
import com.informe.informeapisb.src.serviceList.ServiceListProvider;
import com.informe.informeapisb.src.serviceList.ServiceListService;
import com.informe.informeapisb.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.informe.informeapisb.config.BaseResponseStatus.*;
import static com.informe.informeapisb.utils.ValidationRegex.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;



@RestController
@RequestMapping("/serviceDetail")
public class ServiceDetailController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

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

    @GetMapping("/json")
    public String callApi() throws IOException {
        StringBuilder result = new StringBuilder();

        String urlStr = "https://api.odcloud.kr/api/gov24/v1/serviceDetail?" +
                "page=1&" +
                "perPage=10&" +
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
}