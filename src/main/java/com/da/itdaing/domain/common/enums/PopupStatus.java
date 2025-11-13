package com.da.itdaing.domain.common.enums;

public enum PopupStatus {
    DRAFT,        // 초안 (옵션)
    PENDING,      // 판매자 등록 → 관리자 승인 대기
    APPROVED,     // 승인됨(노출 가능)
    REJECTED,     // 반려됨(사유 보유)
    HIDDEN,       // 숨김(운영상 비노출)
    ENDED         // 종료(기간 만료 등)
}
