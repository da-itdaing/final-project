package com.da.itdaing.domain.popup;

import com.da.itdaing.domain.common.enums.AreaStatus;
import com.da.itdaing.domain.common.enums.UserRole;
import com.da.itdaing.domain.common.enums.ZoneStatus;
import com.da.itdaing.domain.geo.entity.ZoneArea;
import com.da.itdaing.domain.geo.entity.ZoneCell;
import com.da.itdaing.domain.geo.repository.ZoneAreaRepository;
import com.da.itdaing.domain.geo.repository.ZoneCellRepository;
import com.da.itdaing.domain.master.entity.Region;
import com.da.itdaing.domain.master.repository.RegionRepository;
import com.da.itdaing.domain.popup.entity.Popup;
import com.da.itdaing.domain.popup.repository.PopupRepository;
import com.da.itdaing.domain.user.entity.Users;
import com.da.itdaing.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PopupRepositoryTest {

    @Autowired UserRepository userRepository;
    @Autowired RegionRepository regionRepository;
    @Autowired ZoneAreaRepository zoneAreaRepository;
    @Autowired ZoneCellRepository zoneCellRepository;
    @Autowired PopupRepository popupRepository;

    @Test
    void 팝업_리포지토리_저장_조회() {
        // seller
        String uniq = java.util.UUID.randomUUID().toString();
        Users seller = userRepository.save(Users.builder()
            .loginId("seller-" + uniq)  // ← 유니크 보장
            .password("pw")
            .email("s@ex.com")
            .name("셀러").nickname("셀1")
            .role(UserRole.SELLER)
            .build());

        // region
        Region region = regionRepository.save(Region.builder().name("서울").build());

        // area & cell (NOT NULL 안전 필드 채움)
        ZoneArea area = zoneAreaRepository.save(
            ZoneArea.builder()
                .region(region)
                .name("강남구역")
                .polygonGeoJson("{\"type\":\"Polygon\",\"coordinates\":[]}")
                .status(AreaStatus.AVAILABLE)
                .build()
        );
        ZoneCell cell = zoneCellRepository.save(
            ZoneCell.builder()
                .zoneArea(area)
                .owner(seller)
                .label("셀-1")
                .detailedAddress("서울시 강남구 어딘가 123")
                .lat(37.5)
                .lng(127.0)
                .status(ZoneStatus.APPROVED)
                .maxCapacity(100)
                .build()
        );

        // popup
        Popup popup = Popup.builder()
            .owner(seller)
            .region(region)
            .name("리포지토리 팝업")
            .place("셀-1")
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(3))
            .intro("repo test")
            .build();

        // zone 파생 필드 세팅
        popup.setZoneDerivedFields(cell);

        Popup saved = popupRepository.save(popup);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getOwner().getId()).isEqualTo(seller.getId());
        assertThat(saved.getRegion().getId()).isEqualTo(region.getId());
        assertThat(saved.getZoneCell().getId()).isEqualTo(cell.getId());
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();

        Popup found = popupRepository.findById(saved.getId()).orElseThrow();
        assertThat(found.getName()).isEqualTo("리포지토리 팝업");
        assertThat(found.getPlace()).isEqualTo("셀-1");
    }
}
