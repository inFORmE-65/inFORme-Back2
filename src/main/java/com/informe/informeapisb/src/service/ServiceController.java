package com.informe.informeapisb.src.service;

import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.config.BaseResponse;
import com.informe.informeapisb.src.service.model.GetSearchInfoRes;
import com.informe.informeapisb.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service")
public class ServiceController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ServiceProvider serviceProvider;
    @Autowired
    private final ServiceService serviceService;
    @Autowired
    private final JwtService jwtService;

    public ServiceController(ServiceProvider serviceProvider, ServiceService serviceService, JwtService jwtService) {
        this.serviceProvider = serviceProvider;
        this.serviceService = serviceService;
        this.jwtService = jwtService;
    }

    // 정책 검색
    @ResponseBody
    @GetMapping("/search")
    public BaseResponse<List<GetSearchInfoRes>> getSearchServiceList(@RequestParam String word){
        try {
            List<GetSearchInfoRes> getSearchInfoRes = serviceProvider.getSearchInfoResList(word);
            return new BaseResponse<>(getSearchInfoRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
