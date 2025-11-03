package com.da.itdaing.domain.geo;

import com.da.itdaing.domain.master.Region;
import com.da.itdaing.domain.master.RegionRepository;
import com.da.itdaing.testsupport.JpaSliceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@JpaSliceTest
class ZoneCellRepositoryTest {

    @Autowired
    private ZoneCellRepository zoneCellRepository;

    @Autowired
    private ZoneAreaRepository zoneAreaRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Test
    void 존_셀을_저장하고_조회할_수_있다() {
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
            .detailedAddress("광주 남구 송암로 12")
            .lat(35.1467)
            .lng(126.9229)
            .build();

        // when
        ZoneCell saved = zoneCellRepository.save(zoneCell);
        ZoneCell found = zoneCellRepository.findById(saved.getId()).orElseThrow();

        // then
        assertThat(found.getLabel()).isEqualTo("A-1");
        assertThat(found.getDetailedAddress()).isEqualTo("광주 남구 송암로 12");
        assertThat(found.getLat()).isEqualTo(35.1467);
        assertThat(found.getCreatedAt()).isNotNull();
    }
}
