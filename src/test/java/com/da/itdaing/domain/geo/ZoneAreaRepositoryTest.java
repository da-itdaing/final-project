package com.da.itdaing.domain.geo;

import com.da.itdaing.domain.geo.entity.ZoneArea;
import com.da.itdaing.domain.geo.repository.ZoneAreaRepository;
import com.da.itdaing.domain.master.entity.Region;
import com.da.itdaing.domain.master.repository.RegionRepository;
import com.da.itdaing.testsupport.JpaSliceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@JpaSliceTest
class ZoneAreaRepositoryTest {

    @Autowired
    private ZoneAreaRepository zoneAreaRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Test
    void 존_영역을_저장하고_조회할_수_있다() {
        // given
        Region region = Region.builder()
                .name("남구")
                .build();
        regionRepository.save(region);

        ZoneArea zoneArea = ZoneArea.builder()
                .region(region)
                .name("송암동 상권")
                .geometryData("{\"type\":\"Polygon\",\"coordinates\":[...]}")
                .build();

        // when
        ZoneArea saved = zoneAreaRepository.save(zoneArea);
        ZoneArea found = zoneAreaRepository.findById(saved.getId()).orElseThrow();

        // then
        assertThat(found.getName()).isEqualTo("송암동 상권");
        assertThat(found.getRegion().getId()).isEqualTo(region.getId());
        assertThat(found.getCreatedAt()).isNotNull();
    }
}

