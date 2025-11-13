// src/main/java/com/da/itdaing/domain/popup/service/PopupService.java
package com.da.itdaing.domain.popup.service;

import com.da.itdaing.domain.common.enums.PopupStatus;
import com.da.itdaing.domain.common.enums.ZoneStatus;
import com.da.itdaing.domain.file.dto.UploadDtos.UploadImageResponse;
import com.da.itdaing.domain.file.service.UploadService;
import com.da.itdaing.domain.geo.entity.ZoneCell;
import com.da.itdaing.domain.geo.repository.ZoneAreaRepository;
import com.da.itdaing.domain.geo.repository.ZoneCellRepository;
import com.da.itdaing.domain.master.entity.Category;
import com.da.itdaing.domain.master.entity.Feature;
import com.da.itdaing.domain.master.entity.Region;
import com.da.itdaing.domain.master.entity.Style;
import com.da.itdaing.domain.master.repository.CategoryRepository;
import com.da.itdaing.domain.master.repository.FeatureRepository;
import com.da.itdaing.domain.master.repository.RegionRepository;
import com.da.itdaing.domain.master.repository.StyleRepository;
import com.da.itdaing.domain.popup.dto.PopupDtos;
import com.da.itdaing.domain.popup.dto.PopupDtos.*;
import com.da.itdaing.domain.popup.entity.*;
import com.da.itdaing.domain.popup.repository.*;
import com.da.itdaing.domain.user.entity.Users;
import com.da.itdaing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PopupService {

    private final PopupRepository popupRepo;
    private final PopupCategoryRepository popupCategoryRepo;
    private final PopupStyleRepository popupStyleRepo;
    private final PopupFeatureRepository popupFeatureRepo;
    private final PopupImageRepository popupImageRepo;

    private final UserRepository userRepo;
    private final RegionRepository regionRepo;
    private final ZoneCellRepository zoneCellRepo;
    private final ZoneAreaRepository zoneAreaRepo;

    private final CategoryRepository categoryRepo;
    private final StyleRepository styleRepo;
    private final FeatureRepository featureRepo;

    // 업로드 연동
    private final UploadService uploadService;

    /* ================== Create ================== */

    // src/main/java/com/da/itdaing/domain/popup/service/PopupService.java
    @Transactional
    public PopupResponse create(Long sellerId, CreatePopupRequest req) {
        Popup popup = createCore(sellerId, req);

        if (req.getUploadedImages() != null && !req.getUploadedImages().isEmpty()) {
            attachImagesFromUploadedRefs(popup, req.getUploadedImages());
        } else {
            attachImagesFromUrls(popup, req.getImages()); // 외부 URL 케이스
        }
        return toDto(popup);
    }

    @Transactional
    public PopupResponse createWithImages(Long sellerId, CreatePopupRequest req, List<MultipartFile> images) {
        Popup popup = createCore(sellerId, req);
        if (images != null && !images.isEmpty()) {
            List<UploadImageResponse> uploaded = uploadService.uploadImages(images, sellerId);
            attachImagesFromUpload(popup, uploaded);
        }
        return toDto(popup);
    }

    private Popup createCore(Long sellerId, CreatePopupRequest req) {
        Users owner = userRepo.findById(sellerId).orElseThrow();

        validatePeriod(req.getStartDate(), req.getEndDate());

        // ✅ zoneId 또는 zoneCreateSpec 중 하나는 필수
        if (req.getZoneId() == null && req.getZoneCreateSpec() == null) {
            throw new IllegalArgumentException("zoneId 또는 zoneCreateSpec 중 하나는 필수입니다.");
        }

        ZoneCell zone = null;
        if (req.getZoneId() != null) {
            zone = zoneCellRepo.findById(req.getZoneId()).orElseThrow();
            if (!zone.getOwner().getId().equals(sellerId)) {
                throw new IllegalStateException("본인 소유의 존만 선택할 수 있습니다.");
            }
        } else {
            // 신규 셀 생성
            var z = req.getZoneCreateSpec();
            var area = zoneAreaRepo.findById(z.getZoneAreaId())
                .orElseThrow(() -> new IllegalArgumentException("존 영역(zoneArea) 없음"));

            zone = ZoneCell.builder()
                .zoneArea(area)
                .owner(owner)
                .label(z.getLabel())
                .detailedAddress(z.getDetailedAddress())
                .lat(z.getLat())
                .lng(z.getLng())
                .maxCapacity(z.getMaxCapacity())
                .status(ZoneStatus.PENDING) // 최초 승인대기 등 정책 반영
                .build();
            zoneCellRepo.save(zone);
        }

        // Popup 생성
        Popup popup = Popup.builder()
            .owner(owner)
            .name(req.getName())
            .place(req.getPlace())
            .startDate(req.getStartDate())
            .endDate(req.getEndDate())
            .intro(req.getIntro())
            .status(PopupStatus.PENDING)
            .build();

        // ✅ 주소/좌표/지역 파생
        popup.setZoneDerivedFields(zone);

        // 해시태그
        popup.replaceHashtags(req.getHashtags());

        popupRepo.save(popup);

        // ✅ 연결 테이블(양방향 동기화)
        if (req.getCategoryIds() != null) {
            attachCategories(popup, req.getCategoryIds(), "POPUP");
        }
        if (req.getTargetCategoryIds() != null) {
            attachCategories(popup, req.getTargetCategoryIds(), "TARGET");
        }
        attachStyles(popup, req.getStyleIds());
        attachFeatures(popup, req.getFeatureIds());

        return popup;
    }

    /* ================== Update ================== */

    // JSON (이미지 URL 교체)
    public PopupResponse update(Long sellerId, Long popupId, UpdatePopupRequest req) {
        Popup p = updateCore(sellerId, popupId, req);

        // 이미지 교체 (URL)
        if (req.getImages() != null) {
            clearImages(p);
            attachImagesFromUrls(p, req.getImages());
        }
        return toDto(p);
    }

    // 멀티파트 (이미지 재업로드 후 교체)
    public PopupResponse updateWithImages(Long sellerId, Long popupId, UpdatePopupRequest req, List<MultipartFile> images) {
        Popup p = updateCore(sellerId, popupId, req);

        if (images != null) {
            clearImages(p);
            if (!images.isEmpty()) {
                List<UploadImageResponse> uploaded = uploadService.uploadImages(images, sellerId);
                attachImagesFromUpload(p, uploaded);
            }
        }
        return toDto(p);
    }

    private Popup updateCore(Long sellerId, Long popupId, UpdatePopupRequest req) {
        Popup p = loadOwnedPopup(sellerId, popupId);

        if (req.getStartDate() != null || req.getEndDate() != null) {
            validatePeriod(
                req.getStartDate() != null ? req.getStartDate() : p.getStartDate(),
                req.getEndDate()   != null ? req.getEndDate()   : p.getEndDate()
            );
        }

        if (req.getZoneId() != null) {
            ZoneCell zone = zoneCellRepo.findById(req.getZoneId()).orElseThrow();
            if (!zone.getOwner().getId().equals(sellerId)) {
                throw new IllegalStateException("본인 소유의 존만 선택할 수 있습니다.");
            }
            p.setZoneDerivedFields(zone);
        } else if (req.getRegionId() != null) {
            Region region = regionRepo.findById(req.getRegionId()).orElseThrow();
            trySetRegionWithoutZone(p, region);
        }

        p.updateBasic(req.getName(), req.getPlace(), req.getIntro(),
            req.getStartDate(), req.getEndDate());

        if (req.getHashtags() != null) p.replaceHashtags(req.getHashtags());

        if (req.getCategoryIds() != null || req.getTargetCategoryIds() != null) {
            clearCategories(p);
            if (req.getCategoryIds() != null)       attachCategories(p, req.getCategoryIds(), "POPUP");
            if (req.getTargetCategoryIds() != null) attachCategories(p, req.getTargetCategoryIds(), "TARGET");
        }

        if (req.getStyleIds() != null) {
            clearStyles(p);
            attachStyles(p, req.getStyleIds());
        }

        if (req.getFeatureIds() != null) {
            clearFeatures(p);
            attachFeatures(p, req.getFeatureIds());
        }

        return p;
    }

    /* ================== Delete ================== */

    public void delete(Long sellerId, Long popupId) {
        Popup p = loadOwnedPopup(sellerId, popupId);
        popupRepo.delete(p);
    }

    /* ================== Read ================== */

    @Transactional(readOnly = true)
    public PopupResponse getOne(Long userId, Long popupId) {
        Popup p = popupRepo.findById(popupId).orElseThrow();
        return toDto(p);
    }

    @Transactional(readOnly = true)
    public PopupListResponse listMine(Long sellerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Popup> data = popupRepo.findByOwner_Id(sellerId, pageable);
        return toList(data, page, size);
    }

    /* ================== Admin Moderation ================== */

    public void changeStatus(Long adminId, Long popupId, PopupStatus to, String reason) {
        Users admin = userRepo.findById(adminId).orElseThrow(); // 사용
        Popup p = popupRepo.findById(popupId).orElseThrow();

        switch (to) {
            case APPROVED -> p.approve(admin);  // ✅ admin
            case REJECTED -> {
                if (reason == null || reason.isBlank()) {
                    throw new IllegalArgumentException("반려 사유가 필요합니다.");
                }
                p.reject(admin, reason);        // ✅ admin
            }
            case HIDDEN -> p.hide();
            case ENDED  -> p.end();
            case PENDING, DRAFT -> { /* 필요시 제약 */ }
        }
    }

    /* ================== Helpers ================== */

    private void attachImagesFromUploadedRefs(
        Popup p,
        List<UploadedImageRef> refs
    ) {
        if (refs == null || refs.isEmpty()) return;

        boolean anyMarkedThumb = refs.stream().anyMatch(x -> Boolean.TRUE.equals(x.getThumbnail()));
        boolean first = true;

        for (UploadedImageRef r : refs) {
            boolean thumb = anyMarkedThumb ? Boolean.TRUE.equals(r.getThumbnail()) : first;

            PopupImage img = PopupImage.builder()
                .popup(p)
                .imageUrl(r.getUrl())
                .imageKey(r.getKey())
                .isThumbnail(thumb)
                .build();

            p.getImages().add(img);       // 부모 컬렉션 동기화
            popupImageRepo.save(img);     // 명시 저장
            first = false;
        }
    }

    private void validatePeriod(java.time.LocalDate start, java.time.LocalDate end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("시작일은 종료일보다 이후일 수 없습니다.");
        }
    }

    private Popup loadOwnedPopup(Long sellerId, Long popupId) {
        Popup p = popupRepo.findById(popupId).orElseThrow();
        if (!p.getOwner().getId().equals(sellerId)) {
            throw new IllegalStateException("본인 소유의 팝업만 수정/삭제할 수 있습니다.");
        }
        return p;
    }

    private void trySetRegionWithoutZone(Popup p, Region region) {
        if (p.getZoneCell() == null) {
            try {
                var f = Popup.class.getDeclaredField("region");
                f.setAccessible(true);
                f.set(p, region);
            } catch (Exception ignore) {}
        }
    }

    private void attachCategories(Popup p, List<Long> categoryIds, String role) {
        if (categoryIds == null || categoryIds.isEmpty()) return;
        List<Category> cats = categoryRepo.findAllById(categoryIds);
        for (Category c : cats) {
            PopupCategory link = PopupCategory.builder()
                .popup(p)
                .category(c)
                .categoryRole(role)
                .build();

            p.getCategories().add(link);     // ★ 부모 컬렉션 동기화
            popupCategoryRepo.save(link);    // ★ 명시 저장(안전)
        }
    }

    private void clearCategories(Popup p) {
        popupCategoryRepo.deleteAll(p.getCategories());
        p.getCategories().clear();
    }

    private void attachStyles(Popup p, List<Long> styleIds) {
        if (styleIds == null || styleIds.isEmpty()) return;
        List<Style> list = styleRepo.findAllById(styleIds);
        for (Style s : list) {
            PopupStyle link = PopupStyle.builder()
                .popup(p)
                .style(s)
                .build();

            p.getStyles().add(link);         // ★ 부모 컬렉션 동기화
            popupStyleRepo.save(link);       // ★ 명시 저장
        }
    }

    private void clearStyles(Popup p) {
        popupStyleRepo.deleteAll(p.getStyles());
        p.getStyles().clear();
    }

    private void attachFeatures(Popup p, List<Long> featureIds) {
        if (featureIds == null || featureIds.isEmpty()) return;
        List<Feature> list = featureRepo.findAllById(featureIds);
        for (Feature f : list) {
            PopupFeature link = PopupFeature.builder()
                .popup(p)
                .feature(f)
                .build();

            p.getFeatures().add(link);       // ★ 부모 컬렉션 동기화
            popupFeatureRepo.save(link);     // ★ 명시 저장
        }
    }

    private void clearFeatures(Popup p) {
        popupFeatureRepo.deleteAll(p.getFeatures());
        p.getFeatures().clear();
    }

    private void attachImagesFromUrls(Popup p, List<String> urls) {
        if (urls == null || urls.isEmpty()) return;
        boolean first = true;
        for (String u : urls) {
            PopupImage img = PopupImage.builder()
                .popup(p)
                .imageUrl(u)
                .isThumbnail(first)
                .build();

            p.getImages().add(img);          // ★ 부모 컬렉션 동기화
            popupImageRepo.save(img);        // ★ 명시 저장
            first = false;
        }
    }

    private void attachImagesFromUpload(Popup p, List<UploadImageResponse> uploaded) {
        if (uploaded == null || uploaded.isEmpty()) return;
        boolean first = true;
        for (UploadImageResponse u : uploaded) {
            PopupImage img = PopupImage.builder()
                .popup(p)
                .imageUrl(u.getUrl())
                .imageKey(u.getKey())
                .isThumbnail(first)
                .build();

            p.getImages().add(img);          // ★ 부모 컬렉션 동기화
            popupImageRepo.save(img);        // ★ 명시 저장
            first = false;
        }
    }

    private void clearImages(Popup p) {
        // S3/local 정리 (key가 있는 경우만)
        p.getImages().forEach(img -> {
            if (img.getImageKey() != null && !img.getImageKey().isBlank()) {
                uploadService.deleteByKey(img.getImageKey());
            }
        });
        popupImageRepo.deleteAll(p.getImages());
        p.getImages().clear();
    }

    private PopupListResponse toList(Page<Popup> page, int pageIdx, int size) {
        return PopupListResponse.builder()
            .items(page.getContent().stream().map(this::toDto).toList())
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .page(pageIdx)
            .size(size)
            .build();
    }

    private PopupDtos.PopupResponse toDto(Popup p) {
        String thumb = p.getImages().stream()
            .filter(PopupImage::getIsThumbnail)
            .map(PopupImage::getImageUrl)
            .findFirst().orElse(null);

        List<String> urls = p.getImages().stream()
            .map(PopupImage::getImageUrl)
            .toList();

        List<String> hashtags = toHashtagList(p);

        return PopupResponse.builder()
            .id(p.getId())
            .ownerId(p.getOwner().getId())
            .regionId(p.getRegion() != null ? p.getRegion().getId() : null)
            .zoneId(p.getZoneCell() != null ? p.getZoneCell().getId() : null)
            .name(p.getName())
            .place(p.getPlace())
            .address(p.getAddress())
            .lat(p.getLat())
            .lng(p.getLng())
            .startDate(p.getStartDate())
            .endDate(p.getEndDate())
            .intro(p.getIntro())
            .status(p.getStatus())
            .viewCount(p.getViewCount())
            .rejectReason(p.getRejectReason())
            .hashtags(hashtags)
            .images(urls)
            .thumbnailUrl(thumb)
            .build();
    }

    /** 엔티티의 해시태그(List) → 중복 제거 + 입력 순서 유지 → List */
    private List<String> toHashtagList(Popup p) {
        if (p.getHashtags() == null) return List.of();
        // 엔티티가 Set<String>이면 이미 중복 제거되어 있으므로 List로 변환만 하면 됩니다.
        return new ArrayList<>(p.getHashtags());
    }
}
