// 회원가입 요청 DTO
// 회원가입 요청할 때 클라이온트가 보내는 데이터 형식과, 서버가 돌려주는 응답 형식을 정의하는 것

package com.synk.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserSignupRequest {

    @NotBlank(message = "아이디를 입력해주세요")
    @Size(min = 4, max = 50, message = "아이디는 4~50자 사이여야 합니다.")
    private String userId;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min = 6, message = "비밀번호는 6자 이상이어야 합니다")
    private String password;

    @NotBlank(message = "이름을 입력해주세요")
    private String name;
}
