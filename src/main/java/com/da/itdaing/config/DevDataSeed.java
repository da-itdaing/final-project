package com.da.itdaing.config;

import com.da.itdaing.domain.common.enums.AreaStatus;
import com.da.itdaing.domain.common.enums.UserRole;
import com.da.itdaing.domain.geo.entity.ZoneArea;
import com.da.itdaing.domain.geo.repository.ZoneAreaRepository;
import com.da.itdaing.domain.master.entity.Category;
import com.da.itdaing.domain.master.entity.Feature;
import com.da.itdaing.domain.master.entity.Region;
import com.da.itdaing.domain.master.entity.Style;
import com.da.itdaing.domain.master.repository.CategoryRepository;
import com.da.itdaing.domain.master.repository.FeatureRepository;
import com.da.itdaing.domain.master.repository.RegionRepository;
import com.da.itdaing.domain.master.repository.StyleRepository;
import com.da.itdaing.domain.popup.dto.PopupDtos.CreatePopupRequest;
import com.da.itdaing.domain.popup.repository.PopupRepository;
import com.da.itdaing.domain.popup.service.PopupService;
import com.da.itdaing.domain.seller.entity.SellerProfile;
import com.da.itdaing.domain.seller.repository.SellerProfileRepository;
import com.da.itdaing.domain.user.entity.UserPrefCategory;
import com.da.itdaing.domain.user.entity.UserPrefRegion;
import com.da.itdaing.domain.user.entity.UserPrefStyle;
import com.da.itdaing.domain.user.entity.Users;
import com.da.itdaing.domain.user.repository.UserPrefCategoryRepository;
import com.da.itdaing.domain.user.repository.UserPrefRegionRepository;
import com.da.itdaing.domain.user.repository.UserPrefStyleRepository;
import com.da.itdaing.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 로컬 개발용 데이터 시딩 컴포넌트.
 * - 마스터: Category / Style / Region / Feature
 * - 사용자: CONSUMER 1명(취향 1~4개씩 연결), SELLER 2명(그중 1명은 SellerProfile 생성)
 * - 지오: ZoneArea 기본 3개 + 추가 3개
 * - 팝업: seller1 기준 7개 자동 생성
 */
@Slf4j
@Component
@Profile({"local","dev"})
@RequiredArgsConstructor
@Transactional
public class DevDataSeed implements CommandLineRunner {

    // Master repos
    private final CategoryRepository categoryRepository;
    private final StyleRepository styleRepository;
    private final RegionRepository regionRepository;
    private final FeatureRepository featureRepository;

    // Geo repos
    private final ZoneAreaRepository zoneAreaRepository;

    // User repos
    private final UserRepository userRepository;
    private final UserPrefCategoryRepository userPrefCategoryRepository;
    private final UserPrefStyleRepository userPrefStyleRepository;
    private final UserPrefRegionRepository userPrefRegionRepository;

    // Seller repos
    private final SellerProfileRepository sellerProfileRepository;

    private final PasswordEncoder passwordEncoder;

    // Popup 생성용
    private final PopupService popupService;
    private final PopupRepository popupRepository;

    @Override
    public void run(String... args) {
        log.info("===== [DevDataSeed] START =====");
        seedMaster();
        seedAdmin();
        seedUsersAndPrefs();
        seedSellersAndProfile();
        seedGeoAreas();     // 기본 A/B/C
        seedMoreGeoAreas(); // 추가 D/E/F
        seedDemoPopups();   // 팝업 7개
        log.info("===== [DevDataSeed] DONE =====");
    }

    /* =========================
       Master seed
       ========================= */
    private void seedMaster() {
        // Category
        if (categoryRepository.count() == 0) {
            List<String> categories = List.of(
                "패션", "뷰티", "음식", "건강", "공연/전시", "스포츠", "키즈", "아트", "굿즈", "반려동물"
            );
            categories.forEach(name -> categoryRepository.save(Category.builder().name(name).build()));
            log.info("Seeded {} categories", categories.size());
        }

        // Style
        if (styleRepository.count() == 0) {
            List<String> styles = List.of(
                "혼자여도 좋은", "가족과 함께", "친구와 함께", "연인과 함께", "반려동물과 함께",
                "아기자기한", "감성적인", "활기찬", "차분한",
                "체험하기 좋은", "포토존", "레트로/빈티지", "체험가능", "실내", "야외"
            );
            styles.forEach(s -> styleRepository.save(Style.builder().name(s).build()));
            log.info("Seeded {} styles", styles.size());
        }

        // Region
        if (regionRepository.count() == 0) {
            List<String> regions = List.of("남구", "동구", "서구", "북구/광산구");
            regions.forEach(r -> regionRepository.save(Region.builder().name(r).build()));
            log.info("Seeded {} regions", regions.size());
        }

        // Feature
        if (featureRepository.count() == 0) {
            List<String> features = List.of("무료주차", "무료입장", "예약가능", "굿즈판매");
            features.forEach(f -> featureRepository.save(Feature.builder().name(f).build()));
            log.info("Seeded {} features", features.size());
        }
    }

    /* =========================
       Admin seed
       ========================= */
    private void seedAdmin() {
        Users admin = userRepository.findByLoginId("admin").orElse(null);
        if (admin == null) {
            admin = userRepository.save(
                Users.builder()
                    .loginId("admin")
                    .password(passwordEncoder.encode("admin!1234"))
                    .name("관리자")
                    .nickname("슈퍼관리자")
                    .email("admin@example.com")
                    .role(UserRole.ADMIN)
                    .build()
            );
            log.info("Seeded ADMIN: id={}, loginId={}", admin.getId(), admin.getLoginId());
        } else {
            log.info("ADMIN already exists. Leave password as-is (no change).");
        }
    }

    /* =========================
       Users & Preferences seed
       ========================= */
    private void seedUsersAndPrefs() {
        // CONSUMER 1명
        Users consumer = userRepository.findByLoginId("consumer1").orElse(null);
        if (consumer == null) {
            consumer = userRepository.save(
                Users.builder()
                    .loginId("consumer1")
                    .password(passwordEncoder.encode("pass!1234"))
                    .name("김소비")
                    .nickname("소비왕")
                    .email("consumer1@example.com")
                    .ageGroup(20)
                    .mbti("INFP")
                    .role(UserRole.CONSUMER)
                    .build()
            );
            log.info("Seeded CONSUMER user: id={}, loginId={}", consumer.getId(), consumer.getLoginId());
        }

        // 선호 Category (상위 3개)
        if (userPrefCategoryRepository.count() == 0) {
            List<Category> cats = categoryRepository.findAll().stream().limit(3).toList();
            for (Category c : cats) {
                userPrefCategoryRepository.save(
                    UserPrefCategory.builder().user(consumer).category(c).build()
                );
            }
            log.info("Linked {} category prefs to user {}", cats.size(), consumer.getId());
        }

        // 선호 Style (상위 3개)
        if (userPrefStyleRepository.count() == 0) {
            List<Style> styles = styleRepository.findAll().stream().limit(3).toList();
            for (Style s : styles) {
                userPrefStyleRepository.save(UserPrefStyle.builder()
                    .user(consumer)
                    .style(s)
                    .build());
            }
            log.info("Linked {} style prefs to user {}", styles.size(), consumer.getId());
        }

        // 선호 Region (상위 2개)
        if (userPrefRegionRepository.count() == 0) {
            List<Region> regions = regionRepository.findAll().stream().limit(2).toList();
            for (Region r : regions) {
                userPrefRegionRepository.save(UserPrefRegion.builder()
                    .user(consumer)
                    .region(r)
                    .build());
            }
            log.info("Linked {} region prefs to user {}", regions.size(), consumer.getId());
        }
    }

    /* =========================
       Sellers & SellerProfile seed
       ========================= */
    private void seedSellersAndProfile() {
        // SELLER 1 (프로필 있음)
        Users seller1 = userRepository.findByLoginId("seller1").orElse(null);
        if (seller1 == null) {
            seller1 = userRepository.save(
                Users.builder()
                    .loginId("seller1")
                    .password(passwordEncoder.encode("pass!1234"))
                    .name("박판매")
                    .nickname("팝업왕")
                    .email("seller1@example.com")
                    .role(UserRole.SELLER)
                    .build()
            );
            log.info("Seeded SELLER user: id={}, loginId={}", seller1.getId(), seller1.getLoginId());
        }

        // seller1 프로필 없으면 생성
        boolean profileExists = sellerProfileRepository.findByUserId(seller1.getId()).isPresent();
        if (!profileExists) {
            SellerProfile profile = SellerProfile.builder()
                .user(seller1)
                .profileImageUrl("https://cdn.example.com/profiles/seller1.png")
                .introduction("팝업 운영 3년차, 굿즈 전문")
                .activityRegion("광주 남구")
                .snsUrl("https://instagram.com/popup_seller")
                .build();
            sellerProfileRepository.save(profile);
            log.info("Seeded SellerProfile for userId={}", seller1.getId());
        }

        // SELLER 2 (프로필 없음)
        Users seller2 = userRepository.findByLoginId("seller2").orElse(null);
        if (seller2 == null) {
            seller2 = userRepository.save(
                Users.builder()
                    .loginId("seller2")
                    .password(passwordEncoder.encode("pass!1234"))
                    .name("이판매")
                    .nickname("굿즈장인")
                    .email("seller2@example.com")
                    .role(UserRole.SELLER)
                    .build()
            );
            log.info("Seeded SELLER user (no profile): id={}, loginId={}", seller2.getId(), seller2.getLoginId());
        }
    }

    /* =========================
       Geo: ZoneArea seed (기본 3개)
       ========================= */
    private void seedGeoAreas() {
        if (zoneAreaRepository.count() > 0) {
            log.info("ZoneArea already seeded. Skip.");
            return;
        }

        Region anyRegion = regionRepository.findAll().stream().findFirst().orElse(null);

        String polyA = """
            {"type":"Polygon","coordinates":[
              [
                [126.9768,37.5655],
                [126.9800,37.5655],
                [126.9800,37.5678],
                [126.9768,37.5678],
                [126.9768,37.5655]
              ]
            ]}
            """;

        String polyB = """
            {"type":"Polygon","coordinates":[
              [
                [126.9815,37.5635],
                [126.9845,37.5635],
                [126.9845,37.5665],
                [126.9815,37.5665],
                [126.9815,37.5635]
              ]
            ]}
            """;

        String polyC = """
            {"type":"Polygon","coordinates":[
              [
                [126.9730,37.5720],
                [126.9765,37.5720],
                [126.9765,37.5740],
                [126.9730,37.5740],
                [126.9730,37.5720]
              ]
            ]}
            """;

        ZoneArea a = ZoneArea.builder()
            .region(anyRegion)
            .name("A-구역(시청광장)")
            .polygonGeoJson(polyA)
            .status(AreaStatus.AVAILABLE)
            .maxCapacity(120)
            .notice("행사 다수, 소음 주의")
            .build();

        ZoneArea b = ZoneArea.builder()
            .region(anyRegion)
            .name("B-구역(명동입구)")
            .polygonGeoJson(polyB)
            .status(AreaStatus.AVAILABLE)
            .maxCapacity(80)
            .notice("보행량 많음, 전력제한 3kW")
            .build();

        ZoneArea c = ZoneArea.builder()
            .region(anyRegion)
            .name("C-구역(광화문)")
            .polygonGeoJson(polyC)
            .status(AreaStatus.UNAVAILABLE)
            .maxCapacity(60)
            .notice("공사 예정으로 임시 중단")
            .build();

        zoneAreaRepository.saveAll(List.of(a, b, c));

        log.info("Seeded ZoneAreas: {}, {}, {}", a.getName(), b.getName(), c.getName());
        log.info(" -> IDs: A={}, B={}, C={}", a.getId(), b.getId(), c.getId());
    }

    /* =========================
       Geo: ZoneArea seed (추가 3개 D/E/F)
       ========================= */
    private void seedMoreGeoAreas() {
        var all = zoneAreaRepository.findAll();
        boolean hasD = all.stream().anyMatch(z -> "D-구역(덕수궁돌담길)".equals(z.getName()));
        boolean hasE = all.stream().anyMatch(z -> "E-구역(청계천광장)".equals(z.getName()));
        boolean hasF = all.stream().anyMatch(z -> "F-구역(시청옆공원)".equals(z.getName()));

        if (hasD && hasE && hasF) {
            log.info("ZoneArea D/E/F already exist. Skip.");
            return;
        }

        Region anyRegion = regionRepository.findAll().stream().findFirst().orElse(null);

        String polyD = """
        {"type":"Polygon","coordinates":[
          [
            [126.9738,37.5658],
            [126.9760,37.5658],
            [126.9760,37.5676],
            [126.9738,37.5676],
            [126.9738,37.5658]
          ]
        ]}
        """;

        String polyE = """
        {"type":"Polygon","coordinates":[
          [
            [126.9820,37.5685],
            [126.9850,37.5685],
            [126.9850,37.5703],
            [126.9820,37.5703],
            [126.9820,37.5685]
          ]
        ]}
        """;

        String polyF = """
        {"type":"Polygon","coordinates":[
          [
            [126.9770,37.5628],
            [126.9798,37.5628],
            [126.9798,37.5646],
            [126.9770,37.5646],
            [126.9770,37.5628]
          ]
        ]}
        """;

        List<ZoneArea> adds = new ArrayList<>();
        if (!hasD) {
            adds.add(ZoneArea.builder()
                .region(anyRegion)
                .name("D-구역(덕수궁돌담길)")
                .polygonGeoJson(polyD)
                .status(AreaStatus.AVAILABLE)
                .maxCapacity(100)
                .notice("주말 보행량 많음")
                .build());
        }
        if (!hasE) {
            adds.add(ZoneArea.builder()
                .region(anyRegion)
                .name("E-구역(청계천광장)")
                .polygonGeoJson(polyE)
                .status(AreaStatus.AVAILABLE)
                .maxCapacity(200)
                .notice("행사 잦음, 전력 5kW 제한")
                .build());
        }
        if (!hasF) {
            adds.add(ZoneArea.builder()
                .region(anyRegion)
                .name("F-구역(시청옆공원)")
                .polygonGeoJson(polyF)
                .status(AreaStatus.AVAILABLE)
                .maxCapacity(90)
                .notice("야간 소음 주의")
                .build());
        }

        if (!adds.isEmpty()) {
            zoneAreaRepository.saveAll(adds);
            log.info("Seeded extra ZoneAreas: {}", adds.stream().map(ZoneArea::getName).toList());
        }
    }

    /* =========================
       Demo Popups (7개)
       ========================= */
    private void seedDemoPopups() {
        if (popupRepository.count() >= 7) {
            log.info("Popups already seeded (count={}), skip.", popupRepository.count());
            return;
        }

        Users seller1 = userRepository.findByLoginId("seller1").orElse(null);
        if (seller1 == null) {
            log.warn("seller1 not found. Skip seeding popups.");
            return;
        }
        Long sellerId = seller1.getId();

        var areas = zoneAreaRepository.findAll();
        if (areas.isEmpty()) {
            log.warn("No ZoneAreas found. Skip seeding popups.");
            return;
        }

        List<Long> catIds = categoryRepository.findAll().stream().map(Category::getId).toList();
        List<Long> styleIds = styleRepository.findAll().stream().map(Style::getId).toList();
        List<Long> featIds = featureRepository.findAll().stream().map(Feature::getId).toList();

        Random rnd = new Random(42);

        record Seed(String name, String place, String intro, String[] hashtags, int days, int areaIdx) {}

        List<Seed> seeds = List.of(
            new Seed("패션 팝업 위크", "남구 어딘가 101", "F/W 한정 컬렉션 선공개", new String[]{"패션","트렌디"}, 12, 0),
            new Seed("뷰티 페어", "동구 문화거리 22", "메이크업/스킨케어 체험", new String[]{"뷰티","체험가능"}, 10, 1),
            new Seed("굿즈 스페셜샵", "서구 복합몰 3F", "콜라보 굿즈 한정 판매", new String[]{"굿즈","포토존"}, 9, 2),
            new Seed("아트 마켓", "북구갤러리 앞", "로컬 아티스트 전시/판매", new String[]{"아트","감성적인"}, 14, 3),
            new Seed("푸드 팝업", "광산구 광장", "수제 디저트 & 커피", new String[]{"음식","데이트"}, 7, 4),
            new Seed("레트로 빈티지 쇼", "시청 옆 공원", "레트로 의류/소품 플리마켓", new String[]{"레트로/빈티지","포토존"}, 8, 5),
            new Seed("키즈 체험존", "청계천 광장 쪽", "주말 가족 체험 이벤트", new String[]{"키즈","가족과함께"}, 11, 6)
        );

        int areaCount = areas.size();

        for (int i = 0; i < seeds.size(); i++) {
            Seed s = seeds.get(i);
            ZoneArea area = areas.get(Math.min(s.areaIdx(), areaCount - 1));

            LocalDate start = LocalDate.now().plusDays(2 + i);
            LocalDate end = start.plusDays(s.days());

            List<Long> sampleCats   = catIds.isEmpty()   ? List.of() : catIds.stream().limit(2 + (i % 2)).toList();
            List<Long> sampleStyles = styleIds.isEmpty() ? List.of() : styleIds.stream().skip(i % Math.max(1, styleIds.size()/3)).limit(2).toList();
            List<Long> sampleFeats  = featIds.isEmpty()  ? List.of() : featIds.stream().skip(i % Math.max(1, featIds.size()/2)).limit(1 + (i % 2)).toList();

            CreatePopupRequest req = CreatePopupRequest.builder()
                .name(s.name())
                .place(s.place())
                .zoneCreateSpec(CreatePopupRequest.ZoneCreateSpec.builder()
                    .zoneAreaId(area.getId())
                    .label("AUTO-" + (i+1))
                    .detailedAddress(s.place())
                    .lat(37.5655 + (i * 0.001))
                    .lng(126.9780 + (i * 0.001))
                    .maxCapacity(50 + i * 10)
                    .build())
                .startDate(start)
                .endDate(end)
                .intro(s.intro())
                .categoryIds(sampleCats)
                .styleIds(sampleStyles)
                .featureIds(sampleFeats)
                .hashtags(Arrays.asList(s.hashtags())) // String[] -> List<String>
                .images(List.of(
                    "https://picsum.photos/seed/popup" + (i+1) + "/800/600",
                    "https://picsum.photos/seed/popup" + (i+1) + "b/800/600"
                ))
                .build();

            popupService.create(sellerId, req);
        }

        log.info("Seeded {} demo popups for seller1(loginId=seller1)", seeds.size());
    }

    // BCrypt 해시 패턴 체크(필요시 사용)
    private boolean looksEncoded(String pw) {
        if (pw == null) return false;
        return pw.startsWith("$2a$") || pw.startsWith("$2b$") || pw.startsWith("$2y$");
    }
}
