package com.da.itdaing.domain.messaging;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 메시지 첨부파일
 */
@Entity
@Table(name = "message_attachment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @Column(name = "file_url", length = 500, nullable = false)
    private String fileUrl;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Builder
    public MessageAttachment(Message message, String fileUrl, String mimeType) {
        this.message = message;
        this.fileUrl = fileUrl;
        this.mimeType = mimeType;
    }
}

