/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { LoginRequest } from '../models/LoginRequest';
import type { LogoutRequest } from '../models/LogoutRequest';
import type { SignupConsumerRequest } from '../models/SignupConsumerRequest';
import type { SignupSellerRequest } from '../models/SignupSellerRequest';
import type { TokenRefreshRequest } from '../models/TokenRefreshRequest';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class AuthService {
    /**
     * 액세스 토큰 재발급 (Refresh Token)
     * 만료되었거나 곧 만료될 Access Token을 **Refresh Token**으로 갱신합니다.
     * - 본 API는 Authorization 헤더가 필요없고, **refreshToken**을 본문으로 받습니다.
     * - 서버가 새 Access/Refresh 토큰 쌍을 반환합니다.
     *
     * @param requestBody 리프레시 토큰을 담아 요청합니다.
     * @returns any 재발급 성공
     * @throws ApiError
     */
    public static refresh(
        requestBody: TokenRefreshRequest,
    ): CancelablePromise<any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/auth/token/refresh',
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                401: `인증 실패 - 리프레시 토큰이 유효하지 않음/만료됨`,
            },
        });
    }
    /**
     * 판매자 회원가입
     * 팝업스토어 판매자 계정을 생성합니다.
     *
     * 가입된 사용자는 SELLER 역할이 부여되며, 다음 권한을 가집니다:
     * - 팝업스토어 등록 및 관리
     * - 판매자 프로필 관리
     * - 팝업스토어 승인 요청
     * - 고객 리뷰 조회
     *
     * 판매자 프로필 정보(활동 지역, SNS URL, 프로필 이미지, 소개)도 함께 등록됩니다.
     * - 활동 지역은 필수 입력 항목입니다.
     * - SNS URL은 선택 항목이며, 입력 시 유효한 URL 형식이어야 합니다.
     * - 프로필 이미지 URL과 소개는 선택 항목입니다.
     *
     * 로그인 시 JWT 토큰의 role 클레임에는 "ROLE_SELLER"가 설정됩니다.
     *
     * @param requestBody
     * @returns any 회원가입 성공
     * @throws ApiError
     */
    public static signupSeller(
        requestBody: SignupSellerRequest,
    ): CancelablePromise<any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/auth/signup/seller',
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                400: `입력값 검증 실패`,
                409: `이메일 또는 로그인ID 중복`,
            },
        });
    }
    /**
     * 소비자 회원가입
     * 일반 소비자 계정을 생성합니다.
     *
     * 가입된 사용자는 CONSUMER 역할이 부여되며, 다음 권한을 가집니다:
     * - 팝업스토어 검색 및 조회
     * - 위시리스트 관리
     * - 리뷰 작성 및 조회
     * - 소비자 선호 정보 설정
     *
     * 회원가입 시 선호 정보를 함께 등록합니다:
     * - 관심 카테고리: 1~4개 (필수)
     * - 팝업 스타일: 1~4개 (필수)
     * - 선호 지역: 1~2개 (필수)
     *
     * 모든 선호 정보는 마스터 데이터에 존재하는 ID여야 하며, 중복은 자동으로 제거됩니다.
     *
     * 로그인 시 JWT 토큰의 role 클레임에는 "ROLE_CONSUMER"가 설정됩니다.
     *
     * @param requestBody
     * @returns any 회원가입 성공
     * @throws ApiError
     */
    public static signupConsumer(
        requestBody: SignupConsumerRequest,
    ): CancelablePromise<any> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/auth/signup/consumer',
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                400: `입력값 검증 실패 (선호 정보 개수 초과 등)`,
                404: `마스터 데이터 ID 불일치`,
                409: `이메일 또는 로그인ID 중복`,
            },
        });
    }
    /**
     * 로그아웃
     * 현재 로그인 세션을 종료합니다.
     * - **Authorization: Bearer {accessToken}** 헤더가 필요합니다.
     * - 선택적으로 본문에 **refreshToken**을 함께 전달할 수 있습니다(서버에 저장/블랙리스트 전략에 따라 무효화).
     * - 성공 시 본문 없이 **204 No Content**를 반환합니다.
     *
     * @param authorization
     * @param requestBody 선택적으로 refreshToken을 본문에 포함
     * @returns void
     * @throws ApiError
     */
    public static logout(
        authorization?: string,
        requestBody?: LogoutRequest,
    ): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/auth/logout',
            headers: {
                'Authorization': authorization,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                401: `인증 실패 - Authorization 헤더 없음/형식 오류/토큰 무효`,
            },
        });
    }
    /**
     * 로그인
     * 이메일과 비밀번호로 로그인하여 JWT 액세스 토큰을 발급받습니다.
     *
     * JWT 토큰에는 다음 클레임이 포함됩니다:
     * - sub: 사용자 ID
     * - role: ROLE_CONSUMER, ROLE_SELLER, ROLE_ADMIN 중 하나
     * - iss: itdaing-server
     * - iat: 발급 시각 (Unix timestamp)
     * - exp: 만료 시각 (발급 후 24시간)
     *
     * 발급받은 토큰은 Authorization 헤더에 "Bearer {token}" 형식으로 포함하여 인증이 필요한 API를 호출할 수 있습니다.
     *
     * 예시 토큰 페이로드 (디코딩 후):
     * {
         * "sub": "1",
         * "role": "ROLE_CONSUMER",
         * "iss": "itdaing-server",
         * "iat": 1730419200,
         * "exp": 1730505600
         * }
         *
         * @param requestBody
         * @returns any 로그인 성공
         * @throws ApiError
         */
        public static login(
            requestBody: LoginRequest,
        ): CancelablePromise<any> {
            return __request(OpenAPI, {
                method: 'POST',
                url: '/api/auth/login',
                body: requestBody,
                mediaType: 'application/json',
                errors: {
                    401: `인증 실패`,
                },
            });
        }
        /**
         * 내 프로필 조회
         * 인증된 사용자의 프로필 정보를 조회합니다.
         *
         * 이 API는 JWT 토큰 인증이 필요합니다.
         * Authorization 헤더에 "Bearer {token}" 형식으로 토큰을 포함해야 합니다.
         *
         * 응답의 role 필드는 다음 값 중 하나입니다:
         * - CONSUMER: 일반 소비자
         * - SELLER: 팝업스토어 판매자
         * - ADMIN: 시스템 관리자
         *
         * @returns any 조회 성공
         * @throws ApiError
         */
        public static getMyProfile1(): CancelablePromise<any> {
            return __request(OpenAPI, {
                method: 'GET',
                url: '/api/users/me',
                errors: {
                    401: `인증 실패 - 토큰이 없거나 유효하지 않음`,
                    404: `사용자를 찾을 수 없음`,
                },
            });
        }
    }
