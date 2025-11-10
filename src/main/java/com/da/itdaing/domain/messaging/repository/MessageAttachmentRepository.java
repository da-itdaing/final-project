package com.da.itdaing.domain.messaging.repository;

import com.da.itdaing.domain.messaging.entity.MessageAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageAttachmentRepository extends JpaRepository<MessageAttachment, Long> {
}

