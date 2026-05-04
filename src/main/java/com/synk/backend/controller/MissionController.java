package com.synk.backend.controller;

import com.synk.backend.dto.request.MissionCreateRequest;
import com.synk.backend.dto.response.MissionResponse;
import com.synk.backend.service.MissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @PostMapping
    public ResponseEntity<?> createMission(@Valid @RequestBody MissionCreateRequest request){
        MissionResponse response = missionService.createMission(request);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", response,
                "message", "미션이 생성되었습니다"
        ));
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveMissions(@RequestParam Long roomId){
        List<MissionResponse> response = missionService.getActiveMissions(roomId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", response,
                "message", "진행 중인 미션 조회 성공"
        ));
    }
}
