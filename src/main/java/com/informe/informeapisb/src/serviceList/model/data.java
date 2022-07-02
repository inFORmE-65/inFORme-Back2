package com.informe.informeapisb.src.serviceList.model;

public class data {
    //서비스ID string: 공공서비스 고유 식별자
    private String SVC_ID;

    //지원유형 string: 현금, 현물, 이용권, 서비스 등 지원 형태
    private String SupportType;

    //서비스명 string: 공공서비스 명칭
    private String ServiceName;

    //서비스목적 string: 공공서비스 목적
    private String ServicePurpose;

    //지원대상 string: 공공서비스 지원대상
    private String ServiceTarget;

    //선정기준 string: 지원대상 선정기준
    private String TartgetCriteria;

    //지원내용 string: 공공서비스 지원내용
    private String ServiceContent;

    //신청방법 string: 공공서비스 신청방법
    private String ServiceHowApply;

    //신청기한 string: 공공서비스 신청기한
    private String ServiceApplyDue;

    //상세조회URL string: 정부24 공공서비스 안내 페이지 URL
    private String ServiceUrl;

    //소관기관코드 string: 공공서비스 소관기관 행정표준코드
    private String ServiceAgencyCode;

    //소관기관명 string: 공공서비스 소관기관 명칭
    private String ServiceAgencyName;

    //부서명 string: 공공서비스 소관기관의 부서 명칭
    private String ServiceAgencyPartName;

    //조회수 integer: 정부24 공공서비스 안내 페이지 조회수
    private Integer ServiceViewCount;
}
