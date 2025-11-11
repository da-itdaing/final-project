/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
/**
 * 소비자 회원가입 요청(기본정보 + 선호 선택을 한 번에 제출)
 */
export type SignupConsumerRequest = {
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
     * 로그인 ID
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
     * 나이대(10단위 정수)
     */
    ageGroup: 10 | 20 | 30 | 40 | 50 | 60 | 70 | 80 | 90;
    /**
     * MBTI
     */
    mbti?: string;
    /**
     * 관심 카테고리 ID 목록(소비자용, 1~4개)
     */
    interestCategoryIds: Array<number>;
    /**
     * 스타일 ID 목록(1~4개)
     */
    styleIds: Array<number>;
    /**
     * 선호 지역 ID 목록(1~4개)
     */
    regionIds: Array<number>;
    /**
     * 선호 특징 ID 목록(1~4개)
     */
    featureIds: Array<number>;
    ageGroupTens?: boolean;
};

