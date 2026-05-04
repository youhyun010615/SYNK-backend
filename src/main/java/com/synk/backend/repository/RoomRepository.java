package com.synk.backend.repository;

import com.synk.backend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> { // Room entity를 관리하고, 그 엔티티의 ID(PK) 타입은 Long이다.
    Optional<Room> findByCode(String code); // Optional: 찾는 결과가 없을 때, 프로그램이 갑자기 꺼지지 않도 안전하게 감싸주는 역할

    boolean existsByCode(String code);
}
