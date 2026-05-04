package com.synk.backend.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "room_members",
        uniqueConstraints = @UniqueConstraint(columnNames =
                {"user_id", "room_id"})  // 한 명의 유저가 똑같은 방에 두 번 들어가지 않도록 막아줌
)                                        // user_id 와 room_id 쌍이 중복되지 않도록 DB 차원에서 막아주는 역할
@Getter
@NoArgsConstructor
public class RoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩 : 맴버 정보를 가져올 때 실제로 쓸 떄만 DB에서 가져오기.
    @JoinColumn(name = "user_id", nullable = false) // DB 테이블에서 외래(FK) 컬럼 이름 user_id로 바꾼다.
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "join_at", updatable = false)
    private LocalDateTime joinAt;

    @PrePersist
    protected void onCreate(){
        joinAt = LocalDateTime.now();
    }

    @Builder
    public RoomMember(User user, Room room){
        this.user = user;
        this.room = room;
    }
}
