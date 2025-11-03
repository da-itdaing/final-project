package com.da.itdaing.domain.master.controller;

import com.da.itdaing.domain.common.enums.CategoryType;
import com.da.itdaing.domain.master.dto.CategoryResponse;
import com.da.itdaing.domain.master.dto.FeatureResponse;
import com.da.itdaing.domain.master.dto.RegionResponse;
import com.da.itdaing.domain.master.dto.StyleResponse;
import com.da.itdaing.domain.master.service.MasterQueryService;
import com.da.itdaing.global.web.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 마스터 데이터 조회 API (GET 전용)
 */
@Tag(name = "Master Data", description = "마스터 데이터 조회 API")
@RestController
@RequestMapping("/api/master")
@RequiredArgsConstructor
public class MasterQueryController {

    private final MasterQueryService masterQueryService;

    @Operation(
            summary = "지역 목록 조회",
            description = "광주광역시 구 단위 지역 목록을 조회합니다",
            security = {}
    )
    @GetMapping("/regions")
    public ApiResponse<List<RegionResponse>> getRegions() {
        return ApiResponse.success(masterQueryService.getAllRegions());
    }

    @Operation(
            summary = "스타일 목록 조회",
            description = "팝업스토어 스타일 목록을 조회합니다",
            security = {}
    )
    @GetMapping("/styles")
    public ApiResponse<List<StyleResponse>> getStyles() {
        return ApiResponse.success(masterQueryService.getAllStyles());
    }

    @Operation(
            summary = "카테고리 목록 조회",
            description = "카테고리 목록을 조회합니다. type 파라미터로 POPUP 또는 CONSUMER 타입 필터링 가능",
            security = {}
    )
    @GetMapping("/categories")
    public ApiResponse<List<CategoryResponse>> getCategories(
            @Parameter(description = "카테고리 타입 (POPUP|CONSUMER)", example = "POPUP")
            @RequestParam(required = false) CategoryType type
    ) {
        return ApiResponse.success(masterQueryService.getCategories(type));
    }

    @Operation(
            summary = "특징 목록 조회",
            description = "팝업스토어 특징(편의사항) 목록을 조회합니다",
            security = {}
    )
    @GetMapping("/features")
    public ApiResponse<List<FeatureResponse>> getFeatures() {
        return ApiResponse.success(masterQueryService.getAllFeatures());
    }
}

