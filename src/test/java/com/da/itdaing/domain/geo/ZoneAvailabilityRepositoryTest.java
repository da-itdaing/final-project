package com.da.itdaing.domain.geo;

import com.da.itdaing.domain.geo.entity.ZoneArea;
import com.da.itdaing.domain.geo.entity.ZoneAvailability;
import com.da.itdaing.domain.geo.entity.ZoneCell;
import com.da.itdaing.domain.geo.repository.ZoneAreaRepository;
import com.da.itdaing.domain.geo.repository.ZoneAvailabilityRepository;
import com.da.itdaing.domain.geo.repository.ZoneCellRepository;
import com.da.itdaing.domain.master.entity.Region;
import com.da.itdaing.domain.master.repository.RegionRepository;
import com.da.itdaing.testsupport.JpaSliceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JpaSliceTest
class ZoneAvailabilityRepositoryTest {

    @Autowired
    private ZoneAvailabilityRepository zoneAvailabilityRepository;

    @Autowired
    private ZoneCellRepository zoneCellRepository;

    @Autowired
    private ZoneAreaRepository zoneAreaRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Test
    void 존_가용성을_저장하고_조회할_수_있다() {
        // given
        Region region = Region.builder()
                .name("남구")
                .build();
        regionRepository.save(region);

        ZoneArea zoneArea = ZoneArea.builder()
                .region(region)
                .name("송암동 상권")
                .build();
        zoneAreaRepository.save(zoneArea);

        ZoneCell zoneCell = ZoneCell.builder()
                .zoneArea(zoneArea)
                .label("A-1")
                .build();
        zoneCellRepository.save(zoneCell);

        ZoneAvailability availability = ZoneAvailability.builder()
                .zoneCell(zoneCell)
                .startDate(LocalDate.of(2025, 11, 1))
                .endDate(LocalDate.of(2025, 11, 30))
                .dailyPrice(new BigDecimal("50000.00"))
                .maxConcurrentSlots(2)
                .status("ACTIVE")
                .build();

        // when
        ZoneAvailability saved = zoneAvailabilityRepository.save(availability);
        ZoneAvailability found = zoneAvailabilityRepository.findById(saved.getId()).orElseThrow();

        // then
        assertThat(found.getStartDate()).isEqualTo(LocalDate.of(2025, 11, 1));
        assertThat(found.getEndDate()).isEqualTo(LocalDate.of(2025, 11, 30));
        assertThat(found.getDailyPrice()).isEqualByComparingTo(new BigDecimal("50000.00"));
        assertThat(found.getMaxConcurrentSlots()).isEqualTo(2);
        assertThat(found.getCreatedAt()).isNotNull();
    }

    @Test
    void 존_셀의_기간별_가용성을_저장하고_조회할_수_있다() {
        // given
        Region region = Region.builder()
                .name("동구")
                .build();
        regionRepository.save(region);

        ZoneArea zoneArea = ZoneArea.builder()
                .region(region)
                .name("충장로 상권")
                .build();
        zoneAreaRepository.save(zoneArea);

        ZoneCell zoneCell = ZoneCell.builder()
                .zoneArea(zoneArea)
                .label("B-2")
                .detailedAddress("광주 동구 충장로 100")
                .lat(35.1500)
                .lng(126.9200)
                .build();
        zoneCellRepository.save(zoneCell);

        // 11월 기간
        ZoneAvailability nov = ZoneAvailability.builder()
                .zoneCell(zoneCell)
                .startDate(LocalDate.of(2025, 11, 1))
                .endDate(LocalDate.of(2025, 11, 30))
                .dailyPrice(new BigDecimal("60000.00"))
                .maxConcurrentSlots(1)
                .status("ACTIVE")
                .build();
        zoneAvailabilityRepository.save(nov);

        // 12월 기간
        ZoneAvailability dec = ZoneAvailability.builder()
                .zoneCell(zoneCell)
                .startDate(LocalDate.of(2025, 12, 1))
                .endDate(LocalDate.of(2025, 12, 31))
                .dailyPrice(new BigDecimal("70000.00"))
                .maxConcurrentSlots(1)
                .status("ACTIVE")
                .build();
        zoneAvailabilityRepository.save(dec);

        // when
        ZoneAvailability foundNov = zoneAvailabilityRepository.findById(nov.getId()).orElseThrow();
        ZoneAvailability foundDec = zoneAvailabilityRepository.findById(dec.getId()).orElseThrow();

        // then
        assertThat(foundNov.getZoneCell().getId()).isEqualTo(zoneCell.getId());
        assertThat(foundNov.getStartDate()).isEqualTo(LocalDate.of(2025, 11, 1));
        assertThat(foundNov.getDailyPrice()).isEqualByComparingTo(new BigDecimal("60000.00"));

        assertThat(foundDec.getZoneCell().getId()).isEqualTo(zoneCell.getId());
        assertThat(foundDec.getStartDate()).isEqualTo(LocalDate.of(2025, 12, 1));
        assertThat(foundDec.getDailyPrice()).isEqualByComparingTo(new BigDecimal("70000.00"));
    }
}


