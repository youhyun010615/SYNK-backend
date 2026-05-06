package com.synk.backend.controller;

import com.synk.backend.dto.request.SubmissionCreateRequest;
import com.synk.backend.dto.response.SubmissionResponse;
import com.synk.backend.service.SubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    @PostMapping
    public ResponseEntity<?> submit(
            @AuthenticationPrincipal String userId, @Valid @RequestBody SubmissionCreateRequest request) {
        SubmissionResponse response = submissionService.submit(userId, request);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", response,
                "message", "제출 완료"
        ));
    }

    @GetMapping("/missions/{missionId}")
    public ResponseEntity<?> getSubmissions(@PathVariable Long missionId){
        List<SubmissionResponse> response = submissionService.getSubmissions(missionId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", response,
                "message", "제출 현황 조회 성공"
        ));
    }
}
