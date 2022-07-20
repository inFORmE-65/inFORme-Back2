package com.informe.informeapisb.src.news;

import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.config.BaseResponse;
import com.informe.informeapisb.utils.JwtService;
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


@RestController
@RequestMapping("/news")
public class newsController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final newsProvider newsProvider;
    @Autowired
    private final newsService newsService;
    @Autowired
    private final JwtService jwtService;

    public newsController(newsProvider newsProvider, newsService newsService, JwtService jwtService){
        this.newsProvider = newsProvider;
        this.newsService = newsService;
        this.jwtService = jwtService;
    }

}
