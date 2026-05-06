package com.synk.backend.dto.response;

import com.synk.backend.entity.Submission;
import com.synk.backend.entity.enums.SubmissionStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SubmissionResponse {

    private Long id;
    private Long userId;
    private Long missionId;
    private String frontCameraUrl;
    private String backCameraUrl;
    private String videoUrl;
    private String soundUrl;
    private String location;
    private Integer noiseLevel;
    private SubmissionStatus status;
    private LocalDateTime submittedAt;
    private LocalDateTime createdAt;

    public SubmissionResponse(Submission submission) {
        this.id = submission.getId();
        this.userId = submission.getUser().getId();
        this.missionId = submission.getMission().getId();
        this.frontCameraUrl = submission.getFrontCameraUrl();
        this.backCameraUrl = submission.getBackCameraUrl();
        this.videoUrl = submission.getVideoUrl();
        this.soundUrl = submission.getSoundUrl();
        this.location = submission.getLocation();
        this.noiseLevel = submission.getNoiseLevel();
        this.status = submission.getStatus();
        this.submittedAt = submission.getSubmittedAt();
        this.createdAt = submission.getCreatedAt();
    }
}