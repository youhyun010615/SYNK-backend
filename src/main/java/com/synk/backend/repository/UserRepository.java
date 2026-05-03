package com.synk.backend.repository;

import com.synk.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {  // User: 이 저장소는 User entity를 관리할 거야, Long : 그 엔티티의 @Id 타입은 Long이야

    Optional<User> findByUserId(String userId);  // JPA가 메서드 이름을 보고 자동으로 SQL을 생성해주는 쿼리 메서드 기능

    boolean existsByUserId(String userId);  // 존재 여부를 확인하는 메서드, 중복 가입을 방지하기 위해서 사용.
}
