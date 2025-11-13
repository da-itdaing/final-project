// src/main/java/com/da/itdaing/domain/popup/api/SellerPopupController.java
package com.da.itdaing.domain.popup.api;

import com.da.itdaing.domain.popup.dto.PopupDtos.*;
import com.da.itdaing.domain.popup.service.PopupService;
import com.da.itdaing.global.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/sellers/popups")
@RequiredArgsConstructor
@Tag(name = "Popup (Seller)")
@PreAuthorize("hasRole('SELLER')")
public class SellerPopupController {

    private final PopupService popupService;

    @Operation(summary = "팝업 등록 (JSON, 이미지 URL 사용)")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<PopupResponse> createJson(Principal principal,
                                                 @RequestBody @Valid CreatePopupRequest req) {
        Long sellerId = Long.valueOf(principal.getName());
        return ApiResponse.success(popupService.create(sellerId, req));
    }

    @Operation(summary = "팝업 등록 (multipart: meta + images)")
    @PostMapping(value = "/multipart",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<PopupResponse> createMultipart(Principal principal,
                                                      @RequestPart("meta") @Valid CreatePopupRequest meta,
                                                      @RequestPart(name = "images", required = false) List<MultipartFile> images) {
        Long sellerId = Long.valueOf(principal.getName());
        return ApiResponse.success(popupService.createWithImages(sellerId, meta, images));
    }

    @Operation(summary = "내 팝업 목록")
    @GetMapping("/me")
    public ApiResponse<PopupListResponse> listMine(Principal principal,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "20") int size) {
        Long sellerId = Long.valueOf(principal.getName());
        return ApiResponse.success(popupService.listMine(sellerId, page, size));
    }

    @Operation(summary = "팝업 수정 (JSON, 이미지 URL 교체)")
    @PutMapping(value = "/{popupId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<PopupResponse> updateJson(Principal principal,
                                                 @PathVariable Long popupId,
                                                 @RequestBody CreatePopupRequest reqLikeUpdate) {
        Long sellerId = Long.valueOf(principal.getName());
        return ApiResponse.success(popupService.update(sellerId, popupId, reqLikeUpdate.toUpdateRequest()));
    }

    @Operation(summary = "팝업 수정 (multipart: meta + images 교체)")
    @PutMapping(value = "/{popupId}/multipart",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PopupResponse> updateMultipart(Principal principal,
                                                      @PathVariable Long popupId,
                                                      @RequestPart("meta") CreatePopupRequest metaLikeUpdate,
                                                      @RequestPart(name = "images", required = false) List<MultipartFile> images) {
        Long sellerId = Long.valueOf(principal.getName());
        return ApiResponse.success(popupService.updateWithImages(sellerId, popupId, metaLikeUpdate.toUpdateRequest(), images));
    }

    @Operation(summary = "팝업 삭제")
    @DeleteMapping("/{popupId}")
    public ApiResponse<Void> delete(Principal principal, @PathVariable Long popupId) {
        Long sellerId = Long.valueOf(principal.getName());
        popupService.delete(sellerId, popupId);
        return ApiResponse.success(null);
    }
}
