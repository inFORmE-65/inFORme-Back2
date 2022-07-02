package com.informe.informeapisb.src.serviceList;

import com.informe.informeapisb.src.serviceList.model.*;
import com.informe.informeapisb.config.secret.Secret;
import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.config.BaseResponse;
import com.informe.informeapisb.utils.JwtService;
import com.informe.informeapisb.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.informe.informeapisb.config.BaseResponseStatus.*;
import static com.informe.informeapisb.utils.ValidationRegex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import static com.informe.informeapisb.config.BaseResponseStatus.*;

@Service
public class ServiceListService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ServiceListDao serviceListDao;
    private final ServiceListProvider serviceListProvider;
    private final JwtService jwtService;

    @Autowired
    public ServiceListService(ServiceListDao serviceListDao, ServiceListProvider serviceListProvider, JwtService jwtService) {
        this.serviceListDao = serviceListDao;
        this.serviceListProvider = serviceListProvider;
        this.jwtService = jwtService;
    }

    //ServiceList 삽입
    public void createServiceList(int size,List<data> data) throws BaseException {
        try {
            for(int i=0;i<size;i++){
                serviceListDao.insertServiceList(data.get(i));
            }
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
