package com.da.itdaing.domain.popup.service;

import com.da.itdaing.domain.common.enums.*;
import com.da.itdaing.domain.file.dto.UploadDtos.UploadImageResponse;
import com.da.itdaing.domain.file.service.UploadService;
import com.da.itdaing.domain.geo.entity.ZoneArea;
import com.da.itdaing.domain.geo.repository.ZoneAreaRepository;
import com.da.itdaing.domain.master.entity.*;
import com.da.itdaing.domain.master.repository.*;
import com.da.itdaing.domain.popup.dto.PopupDtos.CreatePopupRequest;
import com.da.itdaing.domain.popup.dto.PopupDtos.PopupResponse;
import com.da.itdaing.domain.popup.entity.Popup;
import com.da.itdaing.domain.popup.entity.PopupCategory;
import com.da.itdaing.domain.popup.repository.PopupRepository;
import com.da.itdaing.domain.user.entity.Users;
import com.da.itdaing.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class PopupServiceTest {

    @Autowired private PopupService popupService;

    @Autowired private UserRepository userRepo;
    @Autowired private RegionRepository regionRepo;
    @Autowired private ZoneAreaRepository zoneAreaRepo;
    @Autowired private CategoryRepository categoryRepo;
    @Autowired private StyleRepository styleRepo;
    @Autowired private FeatureRepository featureRepo;
    @Autowired private PopupRepository popupRepo;
    @Autowired private EntityManager em;

    @MockitoBean
    private UploadService uploadService;

    // 픽스처
    private Users seller;
    private Users admin;
    private Region region;
    private ZoneArea zoneArea;
    private Category category1;
    private Category category2;
    private Style style1;
    private Feature feature1;

    @BeforeEach
    void setUp() {
        String uid = UUID.randomUUID().toString().substring(0, 8);

        seller = userRepo.save(Users.builder()
            .loginId("seller-" + uid)
            .password("pw")
            .email("s+" + uid + "@ex.com")
            .name("셀러").nickname("셀1")
            .role(UserRole.SELLER)
            .build());

        admin = userRepo.save(Users.builder()
            .loginId("admin-" + uid)
            .password("pw")
            .email("admin+" + uid + "@ex.com")
            .name("관리자").nickname("운영자")
            .role(UserRole.ADMIN)
            .build());

        region = regionRepo.save(Region.builder().name("남구").build());

        zoneArea = zoneAreaRepo.save(ZoneArea.builder()
            .region(region)
            .name("A구역-" + uid)
            .polygonGeoJson("{\"type\":\"Polygon\",\"coordinates\":[[[126.97,37.56],[126.98,37.56],[126.98,37.57],[126.97,37.57],[126.97,37.56]]]}")
            .status(AreaStatus.AVAILABLE)
            .build());

        category1 = categoryRepo.save(Category.builder().name("패션-" + uid).build());
        category2 = categoryRepo.save(Category.builder().name("뷰티-" + uid).build());
        style1    = styleRepo.save(Style.builder().name("모던-" + uid).build());
        feature1  = featureRepo.save(Feature.builder().name("무료주차-" + uid).build());
    }

    @Test
    @DisplayName("zoneCreateSpec으로 셀+팝업 생성 - 좌표/주소/지역 파생, 상태 PENDING, 이미지 URL 연결")
    void create_zoneCreateSpec_셀_동시생성_및_연결() {
        // given
        var req = CreatePopupRequest.builder()
            .name("패션 팝업")
            .place("남구 어딘가")
            .zoneCreateSpec(CreatePopupRequest.ZoneCreateSpec.builder()
                .zoneAreaId(zoneArea.getId())
                .label("A-1")
                .detailedAddress("광주 남구 어딘가 101")
                .lat(37.5665).lng(126.9780)
                .maxCapacity(50)
                .build())
            .startDate(LocalDate.of(2025, 11, 15))
            .endDate(LocalDate.of(2025, 11, 30))
            .intro("멋진 팝업입니다")
            .categoryIds(List.of(category1.getId()))
            .styleIds(List.of(style1.getId()))
            .featureIds(List.of(feature1.getId()))
            .hashtags(List.of("패션", "트렌디"))
            .images(List.of("https://example.com/img1.jpg", "https://example.com/img2.jpg"))
            .build();

        // when
        PopupResponse response = popupService.create(seller.getId(), req);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getStatus()).isEqualTo(PopupStatus.PENDING);
        assertThat(response.getRegionId()).isEqualTo(region.getId());
        assertThat(response.getAddress()).isEqualTo("광주 남구 어딘가 101");
        assertThat(response.getLat()).isEqualTo(37.5665);
        assertThat(response.getLng()).isEqualTo(126.9780);
        assertThat(response.getZoneId()).isNotNull();

        assertThat(response.getImages()).hasSize(2);
        assertThat(response.getThumbnailUrl()).isEqualTo("https://example.com/img1.jpg");
        assertThat(response.getHashtags()).containsExactlyInAnyOrder("패션", "트렌디");
    }

    @Test
    @DisplayName("멀티파트 생성 - UploadService 통해 URL 매핑, 첫 이미지를 썸네일")
    void createWithImages_멀티파트_업로드_후_URL_매핑() {
        // given
        var req = CreatePopupRequest.builder()
            .name("뷰티 팝업")
            .place("남구 뷰티샵")
            .zoneCreateSpec(CreatePopupRequest.ZoneCreateSpec.builder()
                .zoneAreaId(zoneArea.getId())
                .label("A-2")
                .detailedAddress("광주 남구 202")
                .lat(37.5666).lng(126.9781)
                .maxCapacity(80)
                .build())
            .startDate(LocalDate.of(2025, 11, 20))
            .endDate(LocalDate.of(2025, 12, 10))
            .intro("뷰티 체험 팝업")
            .styleIds(List.of(style1.getId()))
            .hashtags(List.of("뷰티", "힐링"))
            .build();

        MultipartFile mock1 = org.mockito.Mockito.mock(MultipartFile.class);
        MultipartFile mock2 = org.mockito.Mockito.mock(MultipartFile.class);
        List<MultipartFile> files = List.of(mock1, mock2);

        when(uploadService.uploadImages(ArgumentMatchers.any(), ArgumentMatchers.anyLong()))
            .thenReturn(List.of(
                UploadImageResponse.builder().url("https://s3.example.com/u1.jpg").key("k1").build(),
                UploadImageResponse.builder().url("https://s3.example.com/u2.jpg").key("k2").build()
            ));

        // when
        PopupResponse res = popupService.createWithImages(seller.getId(), req, files);

        // then
        assertThat(res.getImages()).containsExactly(
            "https://s3.example.com/u1.jpg", "https://s3.example.com/u2.jpg"
        );
        assertThat(res.getThumbnailUrl()).isEqualTo("https://s3.example.com/u1.jpg");
    }

    @Test
    @DisplayName("승인/반려(changeStatus) - 반려는 reason 필수, 승인 시 반려값 초기화")
    void changeStatus_승인_및_반려_검증() {
        var req = CreatePopupRequest.builder()
            .name("상태 테스트")
            .place("테스트 장소")
            .zoneCreateSpec(CreatePopupRequest.ZoneCreateSpec.builder()
                .zoneAreaId(zoneArea.getId())
                .label("A-3")
                .detailedAddress("남구 303")
                .lat(37.5).lng(126.9)
                .maxCapacity(30)
                .build())
            .startDate(LocalDate.of(2025, 11, 15))
            .endDate(LocalDate.of(2025, 11, 30))
            .intro("상태변경 테스트")
            .build();

        PopupResponse created = popupService.create(seller.getId(), req);
        assertThat(created.getStatus()).isEqualTo(PopupStatus.PENDING);

        // reason 없이 반려 시도 -> 예외
        assertThatThrownBy(() ->
            popupService.changeStatus(admin.getId(), created.getId(), PopupStatus.REJECTED, null)
        ).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("반려 사유가 필요합니다");

        // 정상 반려
        popupService.changeStatus(admin.getId(), created.getId(), PopupStatus.REJECTED, "부적절한 이미지");
        em.flush(); em.clear();

        Popup rejected = popupRepo.findById(created.getId()).orElseThrow();
        assertThat(rejected.getStatus()).isEqualTo(PopupStatus.REJECTED);
        assertThat(rejected.getRejectReason()).isEqualTo("부적절한 이미지");
        assertThat(rejected.getRejectedAt()).isNotNull();

        // 승인으로 바꾸면 반려 정보 초기화
        popupService.changeStatus(admin.getId(), created.getId(), PopupStatus.APPROVED, null);
        em.flush(); em.clear();

        Popup approved = popupRepo.findById(created.getId()).orElseThrow();
        assertThat(approved.getStatus()).isEqualTo(PopupStatus.APPROVED);
        assertThat(approved.getRejectReason()).isNull();
        assertThat(approved.getRejectedAt()).isNull();
    }

    @Test
    @DisplayName("시작일이 종료일보다 이후면 예외")
    void create_기간_검증() {
        var req = CreatePopupRequest.builder()
            .name("기간 오류")
            .place("테스트")
            .zoneCreateSpec(CreatePopupRequest.ZoneCreateSpec.builder()
                .zoneAreaId(zoneArea.getId())
                .label("A-4")
                .detailedAddress("남구 404")
                .lat(37.4).lng(126.8)
                .maxCapacity(10)
                .build())
            .startDate(LocalDate.of(2025, 12, 31))
            .endDate(LocalDate.of(2025, 11, 15))
            .intro("잘못된 기간")
            .build();

        assertThatThrownBy(() -> popupService.create(seller.getId(), req))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("시작일은 종료일보다 이후일 수 없습니다");
    }

    @Test
    @DisplayName("해시태그 null이면 빈 리스트 처리")
    void create_해시태그_null_처리() {
        var req = CreatePopupRequest.builder()
            .name("해시태그 없음")
            .place("테스트 장소")
            .zoneCreateSpec(CreatePopupRequest.ZoneCreateSpec.builder()
                .zoneAreaId(zoneArea.getId())
                .label("A-5")
                .detailedAddress("남구 505")
                .lat(37.3).lng(126.7)
                .maxCapacity(15)
                .build())
            .startDate(LocalDate.of(2025, 11, 15))
            .endDate(LocalDate.of(2025, 11, 30))
            .intro("해시태그 없음")
            .hashtags(null)
            .build();

        PopupResponse res = popupService.create(seller.getId(), req);
        assertThat(res.getHashtags()).isEmpty();
    }

    @Test
    @DisplayName("이미지 null이면 빈 리스트 및 썸네일 null")
    void create_이미지_null_처리() {
        var req = CreatePopupRequest.builder()
            .name("이미지 없음")
            .place("테스트 장소")
            .zoneCreateSpec(CreatePopupRequest.ZoneCreateSpec.builder()
                .zoneAreaId(zoneArea.getId())
                .label("A-6")
                .detailedAddress("남구 606")
                .lat(37.2).lng(126.6)
                .maxCapacity(20)
                .build())
            .startDate(LocalDate.of(2025, 11, 15))
            .endDate(LocalDate.of(2025, 11, 30))
            .intro("이미지 없음")
            .images(null)
            .build();

        PopupResponse res = popupService.create(seller.getId(), req);
        assertThat(res.getImages()).isEmpty();
        assertThat(res.getThumbnailUrl()).isNull();
    }

    @Test
    @DisplayName("타겟 카테고리와 일반 카테고리 분리 저장")
    void create_타겟_카테고리_분리_저장() {
        var req = CreatePopupRequest.builder()
            .name("카테고리 분리 팝업")
            .place("테스트 장소")
            .zoneCreateSpec(CreatePopupRequest.ZoneCreateSpec.builder()
                .zoneAreaId(zoneArea.getId())
                .label("A-7")
                .detailedAddress("남구 707")
                .lat(37.1).lng(126.5)
                .maxCapacity(25)
                .build())
            .startDate(LocalDate.of(2025, 11, 15))
            .endDate(LocalDate.of(2025, 11, 30))
            .intro("카테고리 테스트")
            .categoryIds(List.of(category1.getId()))
            .targetCategoryIds(List.of(category2.getId()))
            .build();

        PopupResponse response = popupService.create(seller.getId(), req);

        // 영속성 컨텍스트 초기화 후 재조회(컬렉션 스냅샷 이슈 방지)
        em.flush(); em.clear();

        Popup popup = popupRepo.findById(response.getId()).orElseThrow();
        assertThat(popup.getCategories()).hasSize(2);
        assertThat(popup.getCategories())
            .extracting(PopupCategory::getCategoryRole)
            .containsExactlyInAnyOrder("POPUP", "TARGET");
    }
}
