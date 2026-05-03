// 로그인 요청 DTO

package com.synk.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserLoginRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String password;
}
