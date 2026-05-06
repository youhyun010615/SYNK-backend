package com.synk.backend.entity;

import com.synk.backend.entity.enums.SubmissionStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.sql.ast.tree.expression.Summarization;

import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
@Getter
@NoArgsConstructor
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Column(name = "front_camera_url")
    private String frontCameraUrl;

    @Column(name = "back_cemera_url")
    private String backCameraUrl;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "sound_url")
    private String soundUrl;

    @Column(name = "location", columnDefinition = "JSON")
    private String location;

    @Column(name = "noise_level")
    private Integer noiseLevel;

    @Enumerated(EnumType.STRING)  // 이 파트 뭔지 모르겠음
    @Column(name = "status")
    private SubmissionStatus status = SubmissionStatus.SUBMITTED;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
    }

    @Builder
    public Submission(User user, Mission mission, String frontCameraUrl, String backCameraUrl, String videoUrl, String soundUrl, String location, Integer noiseLevel){
        this.user = user;
        this.mission = mission;
        this.frontCameraUrl = frontCameraUrl;
        this.backCameraUrl = backCameraUrl;
        this.videoUrl = videoUrl;
        this.soundUrl = soundUrl;
        this.location = location;
        this.noiseLevel = noiseLevel;
        this.status = SubmissionStatus.SUBMITTED;
        this.submittedAt = LocalDateTime.now();
    }
}
