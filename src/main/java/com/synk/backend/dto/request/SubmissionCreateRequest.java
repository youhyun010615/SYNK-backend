package com.synk.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SubmissionCreateRequest {

    @NotNull
    private Long missionId;

    private String frontCameraUrl;
    private String backCameraUrl;
    private String videoUrl;
    private String soundUrl;
    private String location;
    private Integer noiseLevel;


}

