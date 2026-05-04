package com.synk.backend.entity;

import com.synk.backend.entity.enums.MissionStatus;
import com.synk.backend.entity.enums.MissionType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "missions")
@Getter
@NoArgsConstructor
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Enumerated(EnumType.STRING)
    @Column(name = "mission_type", nullable = false)
    private MissionType missionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "mission_status")
    private MissionStatus missionStatus = MissionStatus.PENDING;

    @Column(name = "targeted_at", nullable = false)
    private LocalDateTime targetedAt;

    @Column(name = "deadline", nullable = false)
    private LocalDateTime deadline;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
    }

    @Builder
    public Mission(Room room, MissionType missionType, LocalDateTime targetedAt){
        this.room = room;
        this.missionType = missionType;
        this.missionStatus = MissionStatus.PENDING;
        this.targetedAt = targetedAt;
        this.deadline = targetedAt.plusSeconds(30);
    }
}
