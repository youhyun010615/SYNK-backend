// 외부(앱이나 웹)에서 보낸 요청이 서버에 도착했을 때 가장 먼저 맞이해주는 역할

package com.synk.backend.controller;
import com.synk.backend.dto.request.UserLoginRequest;
import com.synk.backend.dto.request.UserSignupRequest;
import com.synk.backend.dto.response.UserLoginResponse;
import com.synk.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserSignupRequest request){  // ResponseEntity<?> : 사용자에게 응답을 보낼 때 상태 코드(200 OK 등)와 데이터를 함께 담는 봉투 같은 것
        userService.signup(request);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "회원가입이 완료되었습니다"
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest request){ // @Vaild & @RequestBody || @Vaild: 데이터가 형식에 맞는지 검사 (ex: 이메일 형식이 맞는지, 빈칸이 없는지)
        UserLoginResponse response = userService.login(request);  // @RequestBody : 사용자가 보낸 JSON 데이터 (아이디, 비번 등)을 자바 객체로 변환해서 가져온다.
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", response,
                "message", "로그인 성공"
        ));
    }
}
