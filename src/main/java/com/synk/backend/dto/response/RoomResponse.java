package com.synk.backend.dto.response;

import com.synk.backend.entity.Room;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RoomResponse {
    private Long id;
    private String code;
    private int maxMembers;
    private int currentMembers;
    private LocalDateTime createdAt;

    public RoomResponse(Room room){
        this.id = room.getId();
        this.code = room.getCode();
        this.maxMembers = room.getMaxMembers();
        this.currentMembers = room.getMaxMembers();
        this.createdAt = room.getCreatedAt();
    }
}
