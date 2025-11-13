// src/main/java/com/da/itdaing/domain/popup/api/AdminPopupController.java
package com.da.itdaing.domain.popup.api;

import com.da.itdaing.domain.common.enums.PopupStatus;
import com.da.itdaing.domain.popup.dto.PopupDtos.UpdatePopupStatusRequest;
import com.da.itdaing.domain.popup.service.PopupService;
import com.da.itdaing.global.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/admin/popups")
@RequiredArgsConstructor
@Tag(name = "Popup (Admin)")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPopupController {

    private final PopupService popupService;

    @Operation(summary = "팝업 상태 변경 (승인/반려/숨김/종료)")
    @PatchMapping("/{popupId}/status")
    public ApiResponse<Void> changeStatus(
        Principal principal,
        @PathVariable Long popupId,
        @RequestBody @Valid UpdatePopupStatusRequest req
    ) {
        Long adminId = Long.valueOf(principal.getName());
        PopupStatus to = (req.getStatus() != null) ? req.getStatus() : PopupStatus.PENDING;
        popupService.changeStatus(adminId, popupId, to, req.getReason());
        return ApiResponse.success(null);
    }
}
