package com.da.itdaing.domain.master.controller;

import com.da.itdaing.domain.common.enums.CategoryType;
import com.da.itdaing.domain.master.dto.CategoryResponse;
import com.da.itdaing.domain.master.dto.FeatureResponse;
import com.da.itdaing.domain.master.dto.RegionResponse;
import com.da.itdaing.domain.master.dto.StyleResponse;
import com.da.itdaing.domain.master.service.MasterQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MasterQueryController 테스트
 */
@WebMvcTest(MasterQueryController.class)
class MasterQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MasterQueryService masterQueryService;

    @Test
    @WithMockUser
    @DisplayName("지역 목록 조회 - 성공")
    void getRegions_success() throws Exception {
        // given
        List<RegionResponse> regions = List.of(
                RegionResponse.builder().id(1L).name("남구").build(),
                RegionResponse.builder().id(2L).name("동구").build(),
                RegionResponse.builder().id(3L).name("서구").build()
        );
        given(masterQueryService.getAllRegions()).willReturn(regions);

        // when & then
        mockMvc.perform(get("/api/master/regions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(3))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("남구"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].name").value("동구"))
                .andExpect(jsonPath("$.error").doesNotExist());

        verify(masterQueryService).getAllRegions();
    }

    @Test
    @WithMockUser
    @DisplayName("스타일 목록 조회 - 성공")
    void getStyles_success() throws Exception {
        // given
        List<StyleResponse> styles = List.of(
                StyleResponse.builder().id(1L).name("미니멀").build(),
                StyleResponse.builder().id(2L).name("빈티지").build()
        );
        given(masterQueryService.getAllStyles()).willReturn(styles);

        // when & then
        mockMvc.perform(get("/api/master/styles"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].name").value("미니멀"));

        verify(masterQueryService).getAllStyles();
    }

    @Test
    @WithMockUser
    @DisplayName("특징 목록 조회 - 성공")
    void getFeatures_success() throws Exception {
        // given
        List<FeatureResponse> features = List.of(
                FeatureResponse.builder().id(1L).name("무료 주차").build(),
                FeatureResponse.builder().id(2L).name("애견 동반").build()
        );
        given(masterQueryService.getAllFeatures()).willReturn(features);

        // when & then
        mockMvc.perform(get("/api/master/features"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].name").value("무료 주차"));

        verify(masterQueryService).getAllFeatures();
    }

    @Test
    @WithMockUser
    @DisplayName("카테고리 목록 조회 - 필터 없음 (전체)")
    void getCategories_withoutFilter_success() throws Exception {
        // given
        List<CategoryResponse> categories = List.of(
                CategoryResponse.builder().id(1L).name("패션").type(CategoryType.POPUP).build(),
                CategoryResponse.builder().id(2L).name("뷰티").type(CategoryType.POPUP).build(),
                CategoryResponse.builder().id(3L).name("20대").type(CategoryType.CONSUMER).build()
        );
        given(masterQueryService.getCategories(null)).willReturn(categories);

        // when & then
        mockMvc.perform(get("/api/master/categories"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(3))
                .andExpect(jsonPath("$.data[0].name").value("패션"))
                .andExpect(jsonPath("$.data[0].type").value("POPUP"))
                .andExpect(jsonPath("$.data[2].type").value("CONSUMER"));

        verify(masterQueryService).getCategories(null);
    }

    @Test
    @WithMockUser
    @DisplayName("카테고리 목록 조회 - POPUP 타입 필터")
    void getCategories_withPopupTypeFilter_success() throws Exception {
        // given
        List<CategoryResponse> popupCategories = List.of(
                CategoryResponse.builder().id(1L).name("패션").type(CategoryType.POPUP).build(),
                CategoryResponse.builder().id(2L).name("뷰티").type(CategoryType.POPUP).build(),
                CategoryResponse.builder().id(3L).name("식품").type(CategoryType.POPUP).build()
        );
        given(masterQueryService.getCategories(CategoryType.POPUP)).willReturn(popupCategories);

        // when & then
        mockMvc.perform(get("/api/master/categories")
                        .param("type", "POPUP"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(3))
                .andExpect(jsonPath("$.data[0].type").value("POPUP"))
                .andExpect(jsonPath("$.data[1].type").value("POPUP"))
                .andExpect(jsonPath("$.data[2].type").value("POPUP"));

        verify(masterQueryService).getCategories(CategoryType.POPUP);
    }

    @Test
    @WithMockUser
    @DisplayName("카테고리 목록 조회 - CONSUMER 타입 필터")
    void getCategories_withConsumerTypeFilter_success() throws Exception {
        // given
        List<CategoryResponse> consumerCategories = List.of(
                CategoryResponse.builder().id(10L).name("20대").type(CategoryType.CONSUMER).build(),
                CategoryResponse.builder().id(11L).name("30대").type(CategoryType.CONSUMER).build()
        );
        given(masterQueryService.getCategories(CategoryType.CONSUMER)).willReturn(consumerCategories);

        // when & then
        mockMvc.perform(get("/api/master/categories")
                        .param("type", "CONSUMER"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].name").value("20대"))
                .andExpect(jsonPath("$.data[0].type").value("CONSUMER"))
                .andExpect(jsonPath("$.data[1].name").value("30대"))
                .andExpect(jsonPath("$.data[1].type").value("CONSUMER"));

        verify(masterQueryService).getCategories(CategoryType.CONSUMER);
    }
}

