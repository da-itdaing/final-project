package com.da.itdaing.domain.popup.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 팝업스토어 이미지
 */
@Entity
@Table(
    name = "popup_image",
    indexes = @Index(name = "idx_popup_img_thumb", columnList = "popup_id, is_thumbnail")
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PopupImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "popup_id", nullable = false)
    private Popup popup;

    @Column(name = "image_key", length = 500)   // nullable 허용 (과거 URL-only 데이터 호환)
    private String imageKey;

    @Column(name = "image_url", length = 500, nullable = false)
    private String imageUrl;

    @Column(name = "is_thumbnail", nullable = false)
    private Boolean isThumbnail = false;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public PopupImage(Popup popup, String imageUrl, Boolean isThumbnail) {
        this.popup = popup;
        this.imageUrl = imageUrl;
        this.isThumbnail = isThumbnail != null ? isThumbnail : false;
    }
}


