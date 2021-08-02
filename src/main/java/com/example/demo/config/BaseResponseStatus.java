package com.example.demo.config;

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
    SUCCESS_USER_SEARCH(true, 1000, "유저 조회 성공"),
    SUCCESS_USER_EMAIL(true,1000,"회원가입 가능한 이메일 확인 성공"),
    SUCCESS_POST_USER(true,1000,"회원 가입 성공"),
    SUCCESS_LOGIN(true,1000,"로그인 성공"),
    SUCCESS_LOGIN_KAKAO(true,1000,"카카오 로그인 성공"),
    SUCCESS_MODIFY_NEW_MEMBERSHIP(true,1000,"멤버십 등록 성공"),
    SUCCESS_MODIFY_MEMBERSHIP(true,1000,"멤버십 수정 성공"),
    SUCCESS_MODIFY_CREDIT(true,1000,"결제정보 수정 성공"),
    SUCCESS_POST_CREDIT(true,1000,"결제정보 등록 성공"),
    SUCCESS_MODIFY_EMAIL(true,1000,"이메일 수정 성공"),
    SUCCESS_MODIFY_PASSWORD(true,1000,"비밀번호 수정 성공"),
    SUCCESS_MODIFY_PHONE_NUMBER(true,1000,"전화번호 수정 성공"),
    SUCCESS_PAYMENT_DTL_SEARCH(true,1000,"결제상세정보 조회 성공"),
    SUCCESS_PROFILE_SEARCH(true,1000,"프로필 조회 성공"),
    SUCCESS_POST_PROFILE(true,1000,"프로필 등록 성공"),
    SUCCESS_PAYMENT_MEMBERSHIP_SEARCH(true,1000,"결제상세정보 멤버십 조회 성공"),
    SUCCESS_PROFILE_LOCK(true,1000,"프로필 잠금 설정 저장 성공"),
    SUCCESS_PROFILE_LOGIN(true,1000,"프로필 로그인 성공"),
    SUCCESS_VIEWED_SEARCH(true,1000,"시청기록 조회 성공"),
    SUCCESS_MODIFY_VIEWED_HIDDEN(true,1000,"시청기록 숨김 성공"),
    SUCCESS_RATED_SEARCH(true,1000,"평가기록 조회 성공"),
    SUCCESS_MODIFY_RATED_STATUS(true,1000,"평가기록 삭제 성공"),
    SUCCESS_MODIFY_RATED(true,1000,"평가기록 수정 성공"),
    SUCCESS_KEEP_SEARCH(true,1000,"찜목록 조회 성공"),
    SUCCESS_KEEP_POST(true,1000,"찜 등록 성공"),
    SUCCESS_MODIFY_KEEP_STATUS(true,1000,"찜 삭제 성공"),
    SUCCESS_CONTENT_DTL_SEARCH(true,1000,"콘텐츠 상세 조회 성공"),
    SUCCESS_CONTENT_EPISODE(true,1000,"콘텐츠 에피소드 조회 성공"),
    SUCCESS_CONTENT_RELEASE(true,1000,"공개예정 콘텐츠 조회 성공"),
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
    USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_INVALID_PASSWORD(false,2017,"비밀번호는 6-60자 사이어야 합니다."),
    USERS_INVALID_PHONE(false,2023,"전화번호 형식을 확인해주세요."),
    USERS_EMPTY_PASSWORD(false, 2018, "비밀번호를 입력해주세요."),
    USERS_EMPTY_NEW_PASSWORD(false,2019,"새 비밀번호를 입력해주세요."),
    USERS_EMPTY_NEW_PASSWORD_CHECK(false,2020,"새 비밀번호 확인을 입력해주세요."),
    USERS_NO_EQUALS_PASSWORD(false,2021,"입력된 비밀번호가 일치하지 않습니다."),
    USERS_EMPTY_PHONE_NUMBER(false,2022,"전화번호를 입력해주세요."),

    INVALID_PIN(false,2023,"핀번호는 4자리여야 합니다"),
    //[PATH] /users/payment

    EMPTY_CREDIT_NO(false, 2019, "카드 번호를 입력하세요."),
    EMPTY_EXPIRY_DT(false, 2020, "만료 월을 입력하세요."),
    EMPTY_HOLDER_NM(false, 2021, "이름은 반드시 입력해 주셔야 합니다."),
    EMPTY_HOLDER_YEAR(false, 2022, "출생연도를 입력해 주세요."),
    EMPTY_HOLDER_MONTH(false, 2023, "생월을 입력해 주세요."),
    EMPTY_HOLDER_DAY(false, 2024, "생일을 입력해 주세요."),
    EMPTY_PIN(false,2026,"핀번호를 입력하세요"),
    PROFILE_EMPTY_NAME(false,2025,"프로필 이름을 입력하세요."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"비밀번호가 틀렸습니다."),
    FAILED_TO_PROFILE_LOGIN(false,3015,"핀번호가 틀렸습니다."),
    NO_EXIST_USER(false,3017,"없는 아이디입니다. 회원가입을 먼저 진행해주세요."),
    REST_STATUS_USER(false,3015,"휴면 계정 입니다."),
    PASSWORD_NO_EQUALS_ERROR(false, 3016, "비밀번호가 틀렸습니다."),
    NO_EXIST_MEMBERSHIP(false,3018,"존재하지 않는 멤버십 유형입니다."),
    DELETE_STATUS_PROFILE(false,3019,"삭제된 프로필입니다."),
    NO_EXIST_PROFILE(false,3020,"존재하지 않는 프로필입니다. 프로필을 먼저 생성해주세요."),


    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),
    MODIFY_FAIL_MEMBERSHIP(false,4015,"멤버십 수정 실패"),
    MODIFY_FAIL_CREDIT(false,4016,"결제정보 수정 실패"),
    MODIFY_FAIL_EMAIL(false,4017,"이메일 수정 실패"),
    MODIFY_FAIL_PASSWORD(false,4018,"비밀번호 수정 실패"),
    MODIFY_FAIL_PHONE_NUMBER(false,4019,"전화번호 수정 실패"),
    MODIFY_FAIL_LOCK(false,4020,"잠금 설정 수정 실패"),
    MODIFY_FAIL_VIEWED_HIDDEN(false,4021,"시청기록 숨김 실패"),
    MODIFY_FAIL_RATED_STATUS(false,4022,"평가기록 삭제 실패"),
    MODIFY_FAIL_RATED(false,4023,"평가기록 수정 실패"),
    MODIFY_FAIL_KEEP_STATUS(false,4024,"찜 삭제 실패"),
    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),
    PIN_DECRYPTION_ERROR(false,4013,"핀번호 복호화에 실패했습니다"),
    PIN_ENCRYPTION_ERROR(false,4014,"핀번호 암호화에 실패하였습니다.");

    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
