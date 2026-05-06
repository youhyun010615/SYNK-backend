package com.synk.backend.service;

import com.synk.backend.dto.request.SubmissionCreateRequest;
import com.synk.backend.dto.response.SubmissionResponse;
import com.synk.backend.entity.Mission;
import com.synk.backend.entity.Submission;
import com.synk.backend.entity.User;
import com.synk.backend.repository.MissionRepository;
import com.synk.backend.repository.SubmissionRepository;
import com.synk.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final MissionRepository missionRepository;
    private final UserRepository userRepository;

    @Transactional
    public SubmissionResponse submit(String userId, SubmissionCreateRequest request){
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));
        Mission mission = missionRepository.findById(request.getMissionId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 미션입니다"));

        if(submissionRepository.existsByUserAndMission(user, mission)){
            throw new IllegalArgumentException("이미 제출된 미션입니다");
        }

        Submission submission = Submission.builder()
                .user(user)
                .mission(mission)
                .frontCameraUrl(request.getFrontCameraUrl())
                .backCameraUrl(request.getBackCameraUrl())
                .videoUrl(request.getVideoUrl())
                .soundUrl(request.getSoundUrl())
                .location(request.getLocation())
                .noiseLevel(request.getNoiseLevel())
                .build();

        return new SubmissionResponse(submissionRepository.save(submission));
    }

    public List<SubmissionResponse> getSubmissions(Long missionId){
        Mission mission = missionRepository.findById(missionId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 미션입니다"));

        return submissionRepository.findByMission(mission)
                .stream()
                .map(SubmissionResponse::new)
                .collect(Collectors.toList());
    }
}
