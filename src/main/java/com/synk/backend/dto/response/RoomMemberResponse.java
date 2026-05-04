package com.synk.backend.dto.response;

import com.synk.backend.entity.RoomMember;
import lombok.Getter;

@Getter
public class RoomMemberResponse {

    private Long userId;
    private String name;
    private String profileImage;

    public RoomMemberResponse(RoomMember roomMember) {
        this.userId = roomMember.getUser().getId();
        this.name = roomMember.getUser().getName();
        this.profileImage = roomMember.getUser().getProfileImage();
    }
}
