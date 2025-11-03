package com.da.itdaing.domain.master.service;

import com.da.itdaing.domain.common.enums.CategoryType;
import com.da.itdaing.domain.master.CategoryRepository;
import com.da.itdaing.domain.master.FeatureRepository;
import com.da.itdaing.domain.master.RegionRepository;
import com.da.itdaing.domain.master.StyleRepository;
import com.da.itdaing.domain.master.dto.CategoryResponse;
import com.da.itdaing.domain.master.dto.FeatureResponse;
import com.da.itdaing.domain.master.dto.RegionResponse;
import com.da.itdaing.domain.master.dto.StyleResponse;
import com.da.itdaing.domain.master.mapper.CategoryMapper;
import com.da.itdaing.domain.master.mapper.FeatureMapper;
import com.da.itdaing.domain.master.mapper.RegionMapper;
import com.da.itdaing.domain.master.mapper.StyleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 마스터 데이터 조회 서비스 (읽기 전용)
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MasterQueryService {

    private final RegionRepository regionRepository;
    private final StyleRepository styleRepository;
    private final CategoryRepository categoryRepository;
    private final FeatureRepository featureRepository;

    private final RegionMapper regionMapper;
    private final StyleMapper styleMapper;
    private final CategoryMapper categoryMapper;
    private final FeatureMapper featureMapper;

    /**
     * 모든 지역 조회
     */
    public List<RegionResponse> getAllRegions() {
        return regionMapper.toResponseList(regionRepository.findAll());
    }

    /**
     * 모든 스타일 조회
     */
    public List<StyleResponse> getAllStyles() {
        return styleMapper.toResponseList(styleRepository.findAll());
    }

    /**
     * 카테고리 조회 (타입 필터링 옵션)
     */
    public List<CategoryResponse> getCategories(CategoryType type) {
        if (type != null) {
            return categoryMapper.toResponseList(categoryRepository.findByType(type));
        }
        return categoryMapper.toResponseList(categoryRepository.findAll());
    }

    /**
     * 모든 특징 조회
     */
    public List<FeatureResponse> getAllFeatures() {
        return featureMapper.toResponseList(featureRepository.findAll());
    }
}
