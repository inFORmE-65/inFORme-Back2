package com.informe.informeapisb.src.service;

import com.informe.informeapisb.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ServiceDao serviceDao;
    private final ServiceProvider serviceProvider;
    private final JwtService jwtService;

    @Autowired
    public ServiceService(ServiceDao serviceDao, ServiceProvider serviceProvider, JwtService jwtService) {
        this.serviceDao = serviceDao;
        this.serviceProvider = serviceProvider;
        this.jwtService = jwtService;
    }
}
