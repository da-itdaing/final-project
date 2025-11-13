// src/main/java/com/da/itdaing/domain/popup/dto/PopupDtos.java
package com.da.itdaing.domain.popup.dto;

import com.da.itdaing.domain.common.enums.PopupStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class PopupDtos {
    // PopupDtos.CreatePopupRequest 안에 중첩 클래스로
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class UploadedImageRef {
        @NotBlank private String key;  // S3/Local key
        @NotBlank private String url;  // 공개 URL
        private Boolean thumbnail;     // 선택: 썸네일 표시(미지정시 첫 번째)
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreatePopupRequest {
        @NotBlank @Size(max = 100)
        private String name;
        @NotBlank @Size(max = 150)
        private String place;

        /** 기존 셀을 쓰는 경우 */
        private Long zoneId;

        /** 신규 셀을 만드는 경우 */
        @Valid
        private ZoneCreateSpec zoneCreateSpec;

        @NotNull private LocalDate startDate;
        @NotNull private LocalDate endDate;

        @Size(max = 2000) private String intro;

        private List<Long> categoryIds;
        private List<Long> targetCategoryIds;
        private List<Long> styleIds;
        private List<Long> featureIds;

        @Size(max = 20)
        private List<@Size(max = 50) String> hashtags;

        private List<@Size(max = 500) String> images;

        @Size(max = 20)
        private List<@Valid UploadedImageRef> uploadedImages;

        // 기존 메서드 유지 (update 전환)
        public UpdatePopupRequest toUpdateRequest() {
            return UpdatePopupRequest.builder()
                .name(this.name)
                .place(this.place)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .intro(this.intro)
                .categoryIds(this.categoryIds)
                .targetCategoryIds(this.targetCategoryIds)
                .styleIds(this.styleIds)
                .featureIds(this.featureIds)
                .hashtags(this.hashtags)
                .images(this.images)
                .uploadedImages(this.uploadedImages)
                // update는 zone 변경 자주 안 쓰면 생략 가능
                .zoneId(this.zoneId)
                .build();
        }

        @Getter @NoArgsConstructor @AllArgsConstructor @Builder
        public static class ZoneCreateSpec {
            @NotNull private Long zoneAreaId;
            @NotBlank private String label;
            @NotBlank private String detailedAddress;
            @NotNull private Double lat;
            @NotNull private Double lng;
            private Integer maxCapacity;
        }
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdatePopupRequest {
        @Size(max = 100) private String name;
        @Size(max = 150) private String place;
        private LocalDate startDate;
        private LocalDate endDate;
        @Size(max = 2000) private String intro;

        private List<Long> categoryIds;
        private List<Long> targetCategoryIds;
        private List<Long> styleIds;
        private List<Long> featureIds;

        /** Create와 타입 통일(변환 편의) */
        private List<@Size(max = 50) String> hashtags;

        private List<@Size(max = 500) String> images;

        @Size(max = 20)
        private List<@Valid UploadedImageRef> uploadedImages;

        /** zone 변경을 허용하려면 사용 */
        private Long zoneId;
        private Long regionId;
    }

    @Getter @Setter @Builder
    public static class PopupResponse {
        private Long id;
        private Long ownerId;
        private Long regionId;
        private Long zoneId;

        private String name;
        private String place;
        private String address;
        private Double lat;
        private Double lng;

        private LocalDate startDate;
        private LocalDate endDate;

        private String intro;
        private PopupStatus status;

        private long viewCount;
        private String rejectReason;

        private List<String> hashtags;
        private List<String> images;       // URL 리스트
        private String thumbnailUrl;       // 편의 필드
    }

    @Getter @Setter @Builder
    public static class PopupListResponse {
        private List<PopupResponse> items;
        private long totalElements;
        private int totalPages;
        private int page;
        private int size;
    }

    @Getter @Setter
    public static class UpdatePopupStatusRequest {
        @NotNull
        private PopupStatus status;     // APPROVED / REJECTED / HIDDEN / ENDED / ...
        private String reason;          // REJECTED일 때 필수로 사용
    }
}
