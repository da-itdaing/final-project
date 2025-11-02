package com.da.itdaing.domain.geo;

import com.da.itdaing.domain.master.Region;
import com.da.itdaing.global.jpa.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 존 영역 (Zone Area)
 * - 특정 지역(구) 내의 세부 영역
 */
@Entity
@Table(
    name = "zone_area",
    indexes = @Index(name = "idx_zone_area_region", columnList = "region_id")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ZoneArea extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "geometry_data", columnDefinition = "TEXT")
    private String geometryData;

    @Builder
    public ZoneArea(Region region, String name, String geometryData) {
        this.region = region;
        this.name = name;
        this.geometryData = geometryData;
    }
}
