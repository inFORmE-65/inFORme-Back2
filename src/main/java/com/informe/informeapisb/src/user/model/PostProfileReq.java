package com.informe.informeapisb.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostProfileReq {
    private String JA0101;      // 남성
    private String JA0102;      // 여성
    private String birth;       // 생년월일
    private String age;         // 나이

    // 소득분위
    private String JA0201;      // 소득분위 0-50
    private String JA0202;      // 소득분위 51-75
    private String JA0203;      // 소득분위 76-100
    private String JA0204;      // 소득분위 101-200
    private String JA0205;      // 소득분위 200 ~

    // 개인 특성
    private String JA0301;      // 예비부모/난임
    private String JA0302;      // 임신부
    private String JA0303;      // 출산/입양
    private String JA0304;      // 심한 장애
    private String JA0305;      // 심하지 않은 장애
    private String JA0306;      // 독립유공자
    private String JA0307;      // 국가유공자
    private String JA0308;      // 참전유공자
    private String JA0309;      // 보훈보상대상자
    private String JA0310;      // 특수임무유공자
    private String JA0311;      // 5.18민주유공자
    private String JA0312;      // 제대군인
    private String JA0313;      // 농업인
    private String JA0314;      // 어업인
    private String JA0315;      // 축산업인
    private String JA0316;      // 임업인
    private String JA0317;      // 초등학생
    private String JA0318;      // 중학생
    private String JA0319;      // 고등학생
    private String JA0320;      // 대학원/대학원생

    private String JA0322;      // 해당사항없음
    private String JA0323;      // 질병/부상/질환자
    private String JA0324;      // 중증,난치,희귀질환자
    private String JA0325;      // 요양환자/치매환자자
    private String JA0326;      // 근로자/직장인
    private String JA0327;      // 구직자/실업자

    // 가구 특성
    private String JA0401;      // 다문화가족
    private String JA0402;      // 북한이탈주민
    private String JA0403;      // 한부모가정/조손가정
    private String JA0404;      // 1인가구

    private String JA0410;      // 해당사항없음
    private String JA0411;      // 다자녀가구
    private String JA0412;      // 무주택세대
    private String JA0413;      // 신규전입
    private String JA0414;      // 확대가족
}
