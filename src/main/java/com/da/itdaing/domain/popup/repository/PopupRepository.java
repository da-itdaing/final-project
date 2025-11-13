// src/main/java/com/da/itdaing/domain/popup/repository/PopupRepository.java
package com.da.itdaing.domain.popup.repository;

import com.da.itdaing.domain.popup.entity.Popup;
import com.da.itdaing.domain.common.enums.PopupStatus;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopupRepository extends JpaRepository<Popup, Long> {
    Page<Popup> findByOwner_Id(Long ownerId, Pageable pageable);
    Page<Popup> findByStatus(PopupStatus status, Pageable pageable);
}
