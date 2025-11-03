package com.da.itdaing.domain.master;

import com.da.itdaing.domain.common.enums.CategoryType;
import com.da.itdaing.testsupport.JpaSliceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JpaSliceTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void 카테고리를_저장하고_조회할_수_있다() {
        // given
        Category category = Category.builder()
                .name("패션")
                .type(CategoryType.POPUP)
                .build();

        // when
        Category saved = categoryRepository.save(category);
        Category found = categoryRepository.findById(saved.getId()).orElseThrow();

        // then
        assertThat(found.getName()).isEqualTo("패션");
        assertThat(found.getType()).isEqualTo(CategoryType.POPUP);
        assertThat(found.getCreatedAt()).isNotNull();
    }

    @Test
    void 동일한_타입과_이름의_카테고리는_중복_저장할_수_없다() {
        // given
        Category category1 = Category.builder()
                .name("패션")
                .type(CategoryType.POPUP)
                .build();
        categoryRepository.save(category1);

        Category category2 = Category.builder()
                .name("패션")
                .type(CategoryType.POPUP)
                .build();

        // when & then
        assertThatThrownBy(() -> {
            categoryRepository.save(category2);
            categoryRepository.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }
}
