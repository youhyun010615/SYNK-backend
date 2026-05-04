package com.synk.backend.repository;

import com.synk.backend.entity.Room;
import com.synk.backend.entity.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {

    List<RoomMember> findByRoomId(Long roomId); // 특정 방의 ID(roomId)를 던지면, 그 방에 속해 있는 모든 멤버의 목록을 리스트로 돌려줌.

    List<RoomMember> findByUserId(Long userId); // 특정 유저의 ID(userId)를 던지면, 그 사람이 가입된 모든 방 참여 기록을 가져옵니다.

    boolean existsByUserIdAndRoomId(Long userId, Long roomId);

    Optional<RoomMember> findByUserIdAndRoomId(Long userId, Long roomId);
}


