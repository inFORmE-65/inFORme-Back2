package com.informe.informeapisb.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    // 입력 오류
    POST_USERS_EMPTY_NAME(false, 2005, "이름을 입력해주세요"),
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_EMPTY_PHONE(false, 2025, "전화번호를 입력해주세요"),
    POST_USERS_EMPTY_PASSWORD(false, 2035, "비밀번호를 입력해주세요"),
    POST_USERS_EMPTY_NICKNAME(false, 2045, "닉네임을 입력해주세요"),
    POST_USERS_EMPTY_BIRTH(false, 2055, "생년월일을 입력해주세요"),

    // 형식 오류
    POST_USERS_INVALID_NAME(false, 2006, "이름 형식을 확인해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_INVALID_PHONE(false, 2026, "전화번호 형식을 확인해주세요"),
    POST_USERS_INVALID_PASSWORD(false, 2036, "비밀번호 형식을 확인해주세요"),

    // 중복 오류
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),
    POST_USERS_EXISTS_NICKNAME(false, 2047, "중복된 닉네임입니다"),


    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),
    DELETE_FAIL_USER(false, 3015, "유저를 삭제하는데 실패하였습니다"),
    DELETE_FAIL_SCRAP(false, 3016, "스크랩 취소에 실패했습니다"),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),
    JWT_ERROR(false, 4002, "jwt 발급에 실패했습니다."),
    EMAIL_CHECK_ERROR(false, 4003, "이메일 체크에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
