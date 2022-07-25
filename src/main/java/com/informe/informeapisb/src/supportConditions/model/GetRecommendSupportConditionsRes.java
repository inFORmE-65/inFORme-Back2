package com.informe.informeapisb.src.supportConditions.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetRecommendSupportConditionsRes {
    private String SVC_ID;
    private String JA0101;    // 남성
    private String JA0102;    // 여성

    private String JA0110;    // 대상 연령 (시작)
    private String JA0111;    // 대상 연령 (종료)

    /*
    private String JA0203;    // 76 ~ 100
    private String JA0204;    // 101 ~ 200

    private String JA0301;    // 예비부모/난임
    private String JA0302;    // 임신부
    private String JA0303;    // 출산/입양
    private String JA0304;    // 심한 장애

     */

}
