package com.da.itdaing.domain.social.repository;

import com.da.itdaing.domain.social.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
