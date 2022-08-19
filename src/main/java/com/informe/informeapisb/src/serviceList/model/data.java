package com.informe.informeapisb.src.serviceList.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class data {
    //서비스ID string: 공공서비스 고유 식별자
    private String 서비스ID;//SVC_ID;
    //지원유형 string: 현금, 현물, 이용권, 서비스 등 지원 형태
    private String 지원유형;//SupportType;
    //서비스명 string: 공공서비스 명칭
    private String 서비스명;//ServiceName;
    //서비스목적 string: 공공서비스 목적
    private String 서비스목적;//ServicePurpose;
    //지원대상 string: 공공서비스 지원대상
    private String 지원대상;//ServiceTarget;
    //선정기준 string: 지원대상 선정기준
    private String 선정기준;//TargetCriteria;
    //지원내용 string: 공공서비스 지원내용
    private String 지원내용;//ServiceContent;
    //신청방법 string: 공공서비스 신청방법
    private String 신청방법;//ServiceHowApply;
    //신청기한 string: 공공서비스 신청기한
    private String 신청기한;//ServiceApplyDue;
    //상세조회URL string: 정부24 공공서비스 안내 페이지 URL
    private String 상세조회URL;//ServiceUrl;
    //소관기관코드 string: 공공서비스 소관기관 행정표준코드
    private String 소관기관코드;//ServiceAgencyCode;
    //소관기관명 string: 공공서비스 소관기관 명칭
    private String 소관기관명;//ServiceAgencyName;
    //부서명 string: 공공서비스 소관기관의 부서 명칭
    private String 부서명;//ServiceAgencyPartName;
    //조회수 integer: 정부24 공공서비스 안내 페이지 조회수
    private Integer 조회수;//ServiceViewCount;
}
