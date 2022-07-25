package com.informe.informeapisb.src.community;

import com.informe.informeapisb.src.community.model.*;
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
import java.util.List;

@RestController
@RequestMapping("/community")
public class CommunityController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CommunityProvider communityProvider;
    @Autowired
    private final CommunityService communityService;
    @Autowired
    private final JwtService jwtService;

    public CommunityController(CommunityProvider communityProvider, CommunityService communityService, JwtService jwtService){
        this.communityProvider = communityProvider;
        this.communityService = communityService;
        this.jwtService = jwtService;
    }
}
