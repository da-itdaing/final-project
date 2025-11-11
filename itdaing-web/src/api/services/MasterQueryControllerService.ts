/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ApiResponseListCategoryResponse } from '../models/ApiResponseListCategoryResponse';
import type { ApiResponseListFeatureResponse } from '../models/ApiResponseListFeatureResponse';
import type { ApiResponseListRegionResponse } from '../models/ApiResponseListRegionResponse';
import type { ApiResponseListStyleResponse } from '../models/ApiResponseListStyleResponse';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class MasterQueryControllerService {
    /**
     * @returns ApiResponseListStyleResponse OK
     * @throws ApiError
     */
    public static getStyles(): CancelablePromise<ApiResponseListStyleResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/master/styles',
        });
    }
    /**
     * @returns ApiResponseListRegionResponse OK
     * @throws ApiError
     */
    public static getRegions(): CancelablePromise<ApiResponseListRegionResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/master/regions',
        });
    }
    /**
     * @returns ApiResponseListFeatureResponse OK
     * @throws ApiError
     */
    public static getFeatures(): CancelablePromise<ApiResponseListFeatureResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/master/features',
        });
    }
    /**
     * @param type
     * @returns ApiResponseListCategoryResponse OK
     * @throws ApiError
     */
    public static getCategories(
        type?: 'POPUP' | 'CONSUMER',
    ): CancelablePromise<ApiResponseListCategoryResponse> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/master/categories',
            query: {
                'type': type,
            },
        });
    }
}
