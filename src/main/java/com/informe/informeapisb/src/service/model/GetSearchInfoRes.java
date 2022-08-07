package com.informe.informeapisb.src.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetSearchInfoRes {
    private String SVC_ID;
    private String serviceName;
    private String serviceTarget;
    private String serviceContent;
}
