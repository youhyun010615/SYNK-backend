package com.synk.backend.dto.request;

import com.synk.backend.entity.enums.MissionType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MissionCreateRequest {

    @NotNull(message = "방 ID를 입력해주세요")
    private Long roomId;

    @NotNull(message = "미션 타입을 입력해주세요")
    private MissionType missionType;
}
