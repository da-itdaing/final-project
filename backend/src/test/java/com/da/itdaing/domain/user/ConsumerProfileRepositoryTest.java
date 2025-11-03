package com.da.itdaing.domain.user;

import com.da.itdaing.domain.common.enums.UserRole;
import com.da.itdaing.testsupport.JpaSliceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@JpaSliceTest
class ConsumerProfileRepositoryTest {

    @Autowired
    private ConsumerProfileRepository consumerProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 소비자_프로필을_저장하고_조회할_수_있다() {
        // given
        Users user = Users.builder()
                .loginId("consumer1")
                .password("pass")
                .email("consumer@example.com")
                .role(UserRole.CONSUMER)
                .build();
        userRepository.save(user);

        ConsumerProfile profile = ConsumerProfile.builder()
                .user(user)
                .ageGroup(20)
                .build();

        // when
        ConsumerProfile saved = consumerProfileRepository.save(profile);
        ConsumerProfile found = consumerProfileRepository.findById(saved.getUserId()).orElseThrow();

        // then
        assertThat(found.getAgeGroup()).isEqualTo(20);
        assertThat(found.getCreatedAt()).isNotNull();
    }
}

