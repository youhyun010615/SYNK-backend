package com.synk.backend.repository;

import com.synk.backend.entity.Room;
import com.synk.backend.entity.RoomMember;
import com.synk.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {

    List<RoomMember> findByRoom(Room room);

    int countByRoom(Room room);

    List<RoomMember> findByUser(User user);

    boolean existsByUserAndRoom(User user, Room room);

    Optional<RoomMember> findByUserAndRoom(User user, Room room);
}