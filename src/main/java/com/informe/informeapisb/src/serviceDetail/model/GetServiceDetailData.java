package com.informe.informeapisb.src.serviceDetail.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetServiceDetailData {
    //서비스명 string
    private String 서비스명;//ServiceName;
    //서비스목적 string
    private String 서비스목적;//ServicePurpose;
    //신청기한 string
    private String 신청기한;//ServiceApplyDue;
    //지원대상 string
    private String 지원대상;//ServiceTarget;
    //선정기준 string
    private String 선정기준;//TargetCriteria;
    //지원내용 string
    private String 지원내용;//ServiceContent;
    //신청방법 string
    private String 신청방법;//ServiceHowApply;
    //구비서류 string
    private String 구비서류;//RequiredDocuments;
    //접수기관명 string
    private String 접수기관명;//AcceptAgencyName;
    //문의처전화번호 string
    private String 문의처전화번호;//ServiceAgencyPhone;
    //온라인신청사이트URL string
    private String 온라인신청사이트URL;//ServiceUrl;
    //소관기관명 string
    private String 소관기관명;//ServiceAgencyName;
}
