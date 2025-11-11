/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
/**
 * 판매자 회원가입 요청
 */
export type SignupSellerRequest = {
    /**
     * 이메일 주소
     */
    email: string;
    /**
     * 비밀번호 (8-20자)
     */
    password: string;
    /**
     * 비밀번호 확인
     */
    passwordConfirm: string;
    /**
     * 로그인 ID(영문 소문자/숫자/하이픈/언더스코어, 4~20자)
     */
    loginId: string;
    /**
     * 이름
     */
    name: string;
    /**
     * 닉네임
     */
    nickname?: string;
    /**
     * 활동 지역
     */
    activityRegion: string;
    /**
     * SNS URL (선택)
     */
    snsUrl?: string;
    /**
     * 프로필 이미지 URL (선택)
     */
    profileImageUrl?: string;
    /**
     * 소개 (선택)
     */
    introduction?: string;
};

