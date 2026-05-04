package com.synk.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RoomJoinRequest {

    @NotBlank(message = "초대 코드를 입력해주세요")
    private String code;
}
