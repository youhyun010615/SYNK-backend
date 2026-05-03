package com.synk.backend.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {

    @Id  //PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // MySQL의 AUTO_INCREMENT
    private Long id;

    @Column(name="user_id", unique = true, nullable = false, length = 50)
    private String userId;

    @Column(name="user_password", nullable = false)
    private String userPassword;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "profile_image")
    private String ProfileImage;

    @Column(name = "pass_count")
    private int passCount = 0;

    @Column(name = "pass_reset_at")
    private LocalDate passResetAt;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist  // DB에 저장되기 직전에 자동으로 createdAt 생성
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @Builder   //  @Builder → User.builder().userId(...).build() 방식으로 객체 생성 가능
    public User(String userId, String userPassword, String name) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.name = name;
    }


}
