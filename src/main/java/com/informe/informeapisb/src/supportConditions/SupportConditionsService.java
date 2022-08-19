package com.informe.informeapisb.src.supportConditions;

import com.informe.informeapisb.src.supportConditions.model.*;
import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.informe.informeapisb.config.BaseResponseStatus.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupportConditionsService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SupportConditionsDao supportConditionsDao;
    private final SupportConditionsProvider supportConditionsProvider;
    private final JwtService jwtService;

    @Autowired
    public SupportConditionsService(SupportConditionsDao supportConditionsDao, SupportConditionsProvider supportConditionsProvider, JwtService jwtService) {
        this.supportConditionsDao = supportConditionsDao;
        this.supportConditionsProvider = supportConditionsProvider;
        this.jwtService = jwtService;
    }

    //SupportDetail 삽입
    public void createSupportConditions(int size, List<SupportConditionsData> data) throws BaseException {
        try {
            for (int i = 0; i < size; i++) {
                supportConditionsDao.insertSupportConditions(data.get(i));
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
