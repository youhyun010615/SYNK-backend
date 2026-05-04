package com.synk.backend.controller;

import com.synk.backend.dto.request.RoomCreateRequest;
import com.synk.backend.dto.request.RoomJoinRequest;
import com.synk.backend.dto.response.RoomMemberResponse;
import com.synk.backend.dto.response.RoomResponse;
import com.synk.backend.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<?> createRoom(
            @AuthenticationPrincipal String userId, // 지금 이 요청을 보낸 사람이 누구인지 서버가 이미 알고 있으니, 그 사람의 ID(PK)를 바로 가져와줘 라는 뜻
            // AuthenticationPrincipal : 사용자가 로그인을 하면 헤더에 JWT 토큰을 담아서 보낸다. 서버의 필터가 그 토큰을 검사해서 유저를 확인하고 어노테이션을 통해 그 값을 userId 변수에 넣어줌
            @RequestBody RoomCreateRequest request) {
        RoomResponse response = roomService.createRoom(userId, request);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", response,
                "message", "방이 생성되었습니다"
        ));
    }
    @PostMapping("/join")
    public ResponseEntity<?> joinRoom(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody RoomJoinRequest request){ // 사용자가 보낸 초대 코드가 비어있지는 않은지 검사
        RoomResponse response = roomService.joinRoom(userId, request); // 해당 유저를 방 맴버로 등록
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", response,
                "message", "방에 참여했습니다"
        ));
    }
    @GetMapping("/my") // 내 방 목록 조회 : 로그인한 유저 본인의 ID를 서비스로 넘겨서 "내가 들어가 있는 방들을 다 가져옴"
    public ResponseEntity<?> getMyRooms(@AuthenticationPrincipal String userId){
        List<RoomResponse> response = roomService.getMyRooms(userId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", response,
                "message", "내 방 목록 조회 성공"
        ));
    }
    @GetMapping("/{id}/members")
    public ResponseEntity<?> getRoomMembers(@PathVariable Long id){
        List<RoomMemberResponse> response = roomService.getRoomMembers(id);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", response,
                "message", "멤버 목록 조회 성공"
        ));
    }

}
