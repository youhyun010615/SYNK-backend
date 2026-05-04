package com.synk.backend.dto.response;

import com.synk.backend.entity.Mission;
import com.synk.backend.entity.enums.MissionStatus;
import com.synk.backend.entity.enums.MissionType;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class MissionResponse {
    private Long id;
    private Long roomId;
    private MissionType missionType;
    private MissionStatus missionStatus;
    private LocalDateTime targetedAt;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;

    public MissionResponse(Mission mission){
        this.id = mission.getId();
        this.roomId = mission.getRoom().getId();
        this.missionType = mission.getMissionType();
        this.missionStatus = mission.getMissionStatus();
        this.targetedAt = mission.getTargetedAt();
        this.deadline = mission.getDeadline();
        this.createdAt = mission.getCreatedAt();

    }
}
