package com.informe.informeapisb.src.news;

import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import static com.informe.informeapisb.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class newsService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final newsDao newsDao;
    private final newsProvider newsProvider;
    private final JwtService jwtService;

    @Autowired
    public newsService(newsDao newsDao, newsProvider newsProvider, JwtService jwtService) {
        this.newsDao = newsDao;
        this.newsProvider = newsProvider;
        this.jwtService = jwtService;
    }


}
