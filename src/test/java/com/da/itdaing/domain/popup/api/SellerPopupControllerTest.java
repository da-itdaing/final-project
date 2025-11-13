package com.da.itdaing.domain.popup.api;

import com.da.itdaing.domain.common.enums.UserRole;
import com.da.itdaing.domain.geo.entity.ZoneArea;
import com.da.itdaing.domain.geo.entity.ZoneCell;
import com.da.itdaing.domain.geo.repository.ZoneAreaRepository;
import com.da.itdaing.domain.geo.repository.ZoneCellRepository;
import com.da.itdaing.domain.master.entity.Region;
import com.da.itdaing.domain.master.repository.RegionRepository;
import com.da.itdaing.domain.user.entity.Users;
import com.da.itdaing.domain.user.repository.UserRepository;
import com.da.itdaing.testsupport.JpaAuditingTestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(JpaAuditingTestConfig.class)
@Transactional
class SellerPopupControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper om;

    @Autowired UserRepository userRepository;
    @Autowired RegionRepository regionRepository;
    @Autowired ZoneAreaRepository zoneAreaRepository;
    @Autowired ZoneCellRepository zoneCellRepository;

    @Test
    void API_판매자_팝업_등록_JSON() throws Exception {
        // seller
        Users seller = userRepository.save(Users.builder()
            .loginId("seller3").password("pw").email("s3@ex.com")
            .name("셀러3").nickname("셀3").role(UserRole.SELLER).build());

        // region/area/cell
        Region region = regionRepository.save(Region.builder().name("서울").build());
        ZoneArea area = zoneAreaRepository.save(ZoneArea.builder().region(region).name("성동구역").build());
        ZoneCell cell = zoneCellRepository.save(ZoneCell.builder()
            .zoneArea(area).owner(seller).label("셀-3").lat(37.56).lng(127.04).build());

        // request JSON (카테고리/스타일/특징은 생략 가능)
        Map<String, Object> body = Map.of(
            "name", "API 팝업",
            "place", "셀-3",
            "zoneId", cell.getId(),
            "regionId", null,
            "startDate", LocalDate.now().toString(),
            "endDate", LocalDate.now().plusDays(7).toString(),
            "intro", "api test",
            "hashtags", List.of("#api", "#등록"),
            "images", List.of("https://cdn/a.jpg", "https://cdn/b.jpg")
        );

        // principal.username == sellerId (문자열) 이어야 서비스에서 Long.valueOf(principal.getName())가 동작
        var sellerPrincipal = SecurityMockMvcRequestPostProcessors.user(
            seller.getId().toString()).roles("SELLER");

        mockMvc.perform(post("/api/sellers/popups")
                .with(sellerPrincipal)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(body)))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            // ApiResponse.success(data) 구조 가정: $.data.*
            .andExpect(jsonPath("$.data.id", notNullValue()))
            .andExpect(jsonPath("$.data.name").value("API 팝업"))
            .andExpect(jsonPath("$.data.zoneId").value(cell.getId()))
            .andExpect(jsonPath("$.data.regionId").value(region.getId()))
            .andExpect(jsonPath("$.data.images", hasSize(2)))
            .andExpect(jsonPath("$.data.hashtags", hasSize(2)));
    }
}
