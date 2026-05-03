// 로그인과 회원가입의 실제 로직 담당

package com.synk.backend.service;

import com.synk.backend.dto.request.UserLoginRequest;
import com.synk.backend.dto.request.UserSignupRequest;
import com.synk.backend.dto.response.UserLoginResponse;
import com.synk.backend.entity.User;
import com.synk.backend.repository.UserRepository;
import com.synk.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void signup(UserSignupRequest request){  // 새로운 사용자를 우리 서비스의 가족으로 받아들이는 과정
        if(userRepository.existsByUserId(request.getUserId())){
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다;");
        }

        User user = User.builder()
                .userId(request.getUserId())
                .userPassword(passwordEncoder.encode(request.getPassword()))  // passwordEncoder.encode를 사용하여 비밀번호를 암호화
                .name(request.getName())
                .build();

        userRepository.save(user);
    }

    public UserLoginResponse login(UserLoginRequest request){  // 사용자가 보낸 정보가 맞는지 확인하고 '입장권 (JWT)'를 끊어주는 과정
        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다"));

        if(!passwordEncoder.matches(request.getPassword(), user.getUserPassword())){
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다");
        }
        String token = jwtUtil.generateToken(user.getUserId());
        return new UserLoginResponse(token, user.getName());
    }
}
