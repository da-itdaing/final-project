/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ApiResponseSellerProfileResponse } from '../models/ApiResponseSellerProfileResponse';
import type { SellerProfileRequest } from '../models/SellerProfileRequest';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class SellerProfileControllerService {
    /**
     * @returns ApiResponseSellerProfileResponse OK
     * @throws ApiError
     */
    public static getMyProfile(): CancelablePromise<ApiResponseSellerProfileResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/sellers/me/profile',
        });
    }
    /**
     * @param requestBody
     * @returns ApiResponseSellerProfileResponse OK
     * @throws ApiError
     */
    public static upsertMyProfile(
        requestBody: SellerProfileRequest,
    ): CancelablePromise<ApiResponseSellerProfileResponse> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/api/sellers/me/profile',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
}
