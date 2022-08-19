package com.informe.informeapisb.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class GetScrapRes {
    private int userIdx;
    private String SVC_ID;
    private String serviceName;
    private String serviceTarget;
    private String serviceContent;
}
