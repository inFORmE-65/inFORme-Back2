package com.informe.informeapisb.src.serviceList.model.hits;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetHitsServiceListRes {
    private String SVC_ID;
    private String serviceName;
    private String serviceTarget;
    private String serviceContent;
    private String serviceViewCount;
}
