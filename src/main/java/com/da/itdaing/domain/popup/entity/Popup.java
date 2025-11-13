// src/main/java/com/da/itdaing/domain/popup/entity/Popup.java
package com.da.itdaing.domain.popup.entity;

import com.da.itdaing.domain.common.enums.PopupStatus;
import com.da.itdaing.domain.geo.entity.ZoneCell;
import com.da.itdaing.domain.master.entity.Region;
import com.da.itdaing.domain.user.entity.Users;
import com.da.itdaing.global.jpa.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "popup",
    indexes = {
        @Index(name = "idx_popup_owner", columnList = "owner_id"),
        @Index(name = "idx_popup_region", columnList = "region_id"),
        @Index(name = "idx_popup_zone", columnList = "zone_cell_id"),
        @Index(name = "idx_popup_status", columnList = "status")
    })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Popup extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 소유자(판매자) */
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "owner_id", nullable = false)
    private Users owner;

    /** 지역 마스터 */
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "region_id")
    private Region region;

    /** 선택한 존(셀). zone을 통하면 주소/좌표를 채움 */
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "zone_cell_id")
    private ZoneCell zoneCell;

    /** 팝업 이름 */
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    /** 장소명(상호/지명) */
    @Column(name = "place", length = 150, nullable = false)
    private String place;

    /** 상세주소(존에서 유래) */
    @Column(name = "address", length = 255)
    private String address;

    /** 좌표(존에서 유래) */
    @Column(name = "lat") private Double lat;
    @Column(name = "lng") private Double lng;

    /** 운영기간 */
    @Column(name = "start_date", nullable = false) private LocalDate startDate;
    @Column(name = "end_date",   nullable = false) private LocalDate endDate;

    /** 소개글 */
    @Column(name = "intro", length = 2000)
    private String intro;

    /** 상태 */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private PopupStatus status = PopupStatus.PENDING;

    /** 조회수(추후 Redis 집계로 교체 가능) */
    @Column(name = "view_count", nullable = false)
    private long viewCount = 0L;

    /** 반려 사유/감리 */
    @Column(name = "reject_reason", length = 500)
    private String rejectReason;

    private LocalDateTime rejectedAt;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "moderated_by")
    private Users moderatedBy;

    /** 해시태그(간단 저장) */
    @ElementCollection
    @CollectionTable(name = "popup_hashtag", joinColumns = @JoinColumn(name = "popup_id"))
    @Column(name = "tag", length = 50)
    private Set<String> hashtags = new LinkedHashSet<>();

    /** 연결 테이블들 */
    @OneToMany(mappedBy = "popup", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PopupCategory> categories = new LinkedHashSet<>();

    @OneToMany(mappedBy = "popup", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PopupStyle> styles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "popup", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PopupFeature> features = new LinkedHashSet<>();

    @OneToMany(mappedBy = "popup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PopupImage> images = new ArrayList<>();

    @Builder
    public Popup(Users owner, Region region, ZoneCell zoneCell,
                 String name, String place, String address,
                 Double lat, Double lng,
                 LocalDate startDate, LocalDate endDate,
                 String intro, PopupStatus status) {
        this.owner = owner;
        this.region = region;
        this.zoneCell = zoneCell;
        this.name = name;
        this.place = place;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.startDate = startDate;
        this.endDate = endDate;
        this.intro = intro;
        this.status = status != null ? status : PopupStatus.PENDING;
    }

    /* ========= 테스트/레거시 호환 alias ========= */

    /** 예전 코드(.seller(...)) 호환용: Lombok이 생성하는 PopupBuilder에 커스텀 메서드 추가 */
    public static class PopupBuilder {
        public PopupBuilder seller(Users seller) {
            return this.owner(seller);
        }
    }

    /** 예전 코드(getSeller()) 호환용 */
    public Users getSeller() {
        return this.owner;
    }

    /* ========= 도메인 로직 ========= */

    public void setZoneDerivedFields(ZoneCell z) {
        this.zoneCell = z;
        if (z != null) {
            this.address = z.getDetailedAddress();
            this.lat = z.getLat();
            this.lng = z.getLng();
            if (z.getZoneArea() != null) {
                this.region = z.getZoneArea().getRegion();
            }
        }
    }

    public void updateBasic(String name, String place, String intro,
                            LocalDate start, LocalDate end) {
        if (name != null) this.name = name;
        if (place != null) this.place = place;
        if (intro != null) this.intro = intro;
        if (start != null) this.startDate = start;
        if (end != null) this.endDate = end;
    }

    public void replaceHashtags(Collection<String> tags) {
        this.hashtags.clear();
        if (tags != null) {
            for (String t : tags) {
                if (t != null && !t.isBlank()) this.hashtags.add(t.trim());
            }
        }
    }

    public void viewOnce() { this.viewCount += 1; }

    public void approve(Users admin) {
        this.status = PopupStatus.APPROVED;
        this.rejectReason = null;
        this.rejectedAt = null;
        this.moderatedBy = admin;
    }

    public void reject(Users admin, String reason) {
        this.status = PopupStatus.REJECTED;
        this.rejectReason = reason;
        this.rejectedAt = LocalDateTime.now();
        this.moderatedBy = admin;
    }

    public void hide() { this.status = PopupStatus.HIDDEN; }

    public void end()  { this.status = PopupStatus.ENDED; }
}
