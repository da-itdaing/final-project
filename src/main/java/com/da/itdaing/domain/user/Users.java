package com.da.itdaing.domain.user;

import com.da.itdaing.domain.common.enums.UserRole;
import com.da.itdaing.global.jpa.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 기본 정보
 */
@Entity
@Table(
    name = "users",
    indexes = @Index(name = "idx_users_role", columnList = "role")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", length = 100, nullable = false, unique = true)
    private String loginId;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "nickname", length = 100)
    private String nickname;

    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;

    @Column(name = "age_group")        // 10,20,30,... (정수, 10단위)
    private Integer ageGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20, nullable = false)
    private UserRole role;

    @Builder
    public Users(String loginId, Integer ageGroup, String password, String name, String nickname, String email, UserRole role) {
        this.loginId = loginId;
        this.ageGroup = ageGroup;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
    }
}
