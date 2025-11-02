package com.da.itdaing.domain.master;

import com.da.itdaing.domain.common.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // CategoryType 필드 기반으로 조회
    List<Category> findByType(CategoryType type);
}
