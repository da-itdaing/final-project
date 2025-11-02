package com.da.itdaing.domain.user;

import com.da.itdaing.global.jpa.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 소비자 프로필
 * - 1:1 관계로 users 테이블과 매핑
 */
@Entity
@Table(name = "consumer_profile")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConsumerProfile extends BaseTimeEntity {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "age_group")
    private Integer ageGroup;

    @Builder
    public ConsumerProfile(Users user, Integer ageGroup) {
        this.user = user;
        this.ageGroup = ageGroup;
    }
}

