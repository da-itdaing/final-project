package com.da.itdaing.domain.messaging.repository;

import com.da.itdaing.domain.messaging.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}

