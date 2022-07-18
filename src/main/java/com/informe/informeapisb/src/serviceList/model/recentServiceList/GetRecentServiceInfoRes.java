package com.informe.informeapisb.src.serviceList.model.recentServiceList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetRecentServiceInfoRes {
    private String SVC_ID;
    private String serviceName;
    private String serviceTarget;
    private String serviceContent;
    private String updatedAt;
}
