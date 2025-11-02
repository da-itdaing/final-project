package com.da.itdaing.domain.geo;

import com.da.itdaing.global.jpa.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 존 셀 (Zone Cell)
 * - Zone Area 내의 개별 셀 (임대 가능한 최소 단위)
 */
@Entity
@Table(
    name = "zone_cell",
    indexes = @Index(name = "idx_zone_cell_area", columnList = "zone_area_id")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ZoneCell extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_area_id", nullable = false)
    private ZoneArea zoneArea;

    @Column(name = "label", length = 100)
    private String label;

    @Column(name = "detailed_address", length = 255)
    private String detailedAddress;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

    @Builder
    public ZoneCell(ZoneArea zoneArea, String label, String detailedAddress, Double lat, Double lng) {
        this.zoneArea = zoneArea;
        this.label = label;
        this.detailedAddress = detailedAddress;
        this.lat = lat;
        this.lng = lng;
    }
}

