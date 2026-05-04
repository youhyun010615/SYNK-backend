package com.synk.backend.repository;

import com.synk.backend.entity.Mission;
import com.synk.backend.entity.Room;
import com.synk.backend.entity.enums.MissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    List<Mission> findByRoom(Room room);

    Optional<Mission> findByRoomAndMissionStatus(Room room, MissionStatus status);
}
