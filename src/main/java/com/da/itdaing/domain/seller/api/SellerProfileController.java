// src/main/java/com/da/itdaing/domain/seller/api/SellerProfileController.java
package com.da.itdaing.domain.seller.api;

import com.da.itdaing.domain.seller.dto.SellerProfileRequest;
import com.da.itdaing.domain.seller.dto.SellerProfileResponse;
import com.da.itdaing.domain.seller.service.SellerProfileService;
// Unified ApiResponse (use global.web.ApiResponse only)
import com.da.itdaing.global.web.ApiResponse;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController // <-- 반드시 RestController
@RequestMapping("/api/sellers/me")
@RequiredArgsConstructor
public class SellerProfileController {

    private final SellerProfileService sellerProfileService;

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<SellerProfileResponse>> getMyProfile(Principal principal) {
        Long userId = Long.valueOf(principal.getName()); // MvcNoSecurityTest에서 넣은 "1"
        SellerProfileResponse resp = sellerProfileService.getMyProfile(userId);
        return ResponseEntity.ok(ApiResponse.success(resp)); // 명시적 ResponseEntity로 JSON 직렬화 보장
    }

    @PutMapping(
        value = "/profile",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiResponse<SellerProfileResponse>> upsertMyProfile(
        Principal principal,
        @Valid @RequestBody SellerProfileRequest req
    ) {
        Long userId = Long.valueOf(principal.getName());
        SellerProfileResponse resp = sellerProfileService.upsertMyProfile(userId, req);
        return ResponseEntity.ok(ApiResponse.success(resp));
    }
}
