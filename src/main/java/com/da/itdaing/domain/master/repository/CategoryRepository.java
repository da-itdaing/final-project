package com.da.itdaing.domain.master.repository;

import com.da.itdaing.domain.master.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
