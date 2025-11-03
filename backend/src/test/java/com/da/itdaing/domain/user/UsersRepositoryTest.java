package com.da.itdaing.domain.user;

import com.da.itdaing.domain.common.enums.UserRole;
import com.da.itdaing.testsupport.JpaSliceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JpaSliceTest
class UsersRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

    @Test
    void 사용자를_저장하고_조회할_수_있다() {
        // given
        Users user = Users.builder()
            .loginId("testuser")
            .password("password123")
            .name("홍길동")
            .nickname("길동이")
            .email("test@example.com")
            .role(UserRole.USER)
            .build();

        // when
        Users saved = usersRepository.save(user);
        Users found = usersRepository.findById(saved.getId()).orElseThrow();

        // then
        assertThat(found.getLoginId()).isEqualTo("testuser");
        assertThat(found.getEmail()).isEqualTo("test@example.com");
        assertThat(found.getRole()).isEqualTo(UserRole.USER);
        assertThat(found.getCreatedAt()).isNotNull();
    }

    @Test
    void 동일한_이메일로_중복_가입할_수_없다() {
        // given
        Users user1 = Users.builder()
            .loginId("user1")
            .password("pass1")
            .email("same@example.com")
            .role(UserRole.USER)
            .build();
        usersRepository.save(user1);

        Users user2 = Users.builder()
            .loginId("user2")
            .password("pass2")
            .email("same@example.com")
            .role(UserRole.USER)
            .build();

        // when & then
        assertThatThrownBy(() -> {
            usersRepository.save(user2);
            usersRepository.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void 동일한_로그인ID로_중복_가입할_수_없다() {
        // given
        Users user1 = Users.builder()
            .loginId("samelogin")
            .password("pass1")
            .email("user1@example.com")
            .role(UserRole.USER)
            .build();
        usersRepository.save(user1);

        Users user2 = Users.builder()
            .loginId("samelogin")
            .password("pass2")
            .email("user2@example.com")
            .role(UserRole.USER)
            .build();

        // when & then
        assertThatThrownBy(() -> {
            usersRepository.save(user2);
            usersRepository.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }
}
