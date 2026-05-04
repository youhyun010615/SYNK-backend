package com.synk.backend.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity  // 이 클래스는 데이터베이스의 테이블과 연결되는 핵심 설계도이다.
@Table( name="rooms" )  // DB에 이 정보를 저장할 때 테이블 이름을 rooms 라고 만들라는 명령어
@Getter
@NoArgsConstructor
public class Room {

    @Id // Pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 번호를 우리가 안 매겨도 DB 가 알아서 순서대로 증가시켜줌
    private Long id;

    @Column(unique = true, nullable = false, length = 10)
    private String code;  // 방에 입장할 때 "입장 코드"

    @Column(name = "max_members")
    private int maxMembers = 10;

    @Column(name = "created_at", updatable = false) // 한 번 지정되면 나중에 수정(Update) 불가
    private LocalDateTime createdAt;  // 방이 생성된 날짜와 시간을 지정

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL) // Room 하나에 RoomMember 여러 명 관계 (1:N 관계)
    private List<RoomMember> roomMembers = new ArrayList<>();  // 이 방에 들어와 있는 맴버들의 목록을 담는 리스트

    @PrePersist  // DB에 데이터를 처음 집어넣기 직전에 이 메서드를 실행
    protected void onCreate(){
        createdAt = LocalDateTime.now();  // 방이 생성되는 그 순간의 현재 시간을 createdAt에 자동으로 넣어준다.
    }

    @Builder  // 빌더 패턴을 사용할 수 있게 해준다.
    public Room(String code, int maxMembers){  // 방을 만들 때 코드와 최대 인원수를 받아서 설정해주는 생성자
        this.code = code;
        this.maxMembers = maxMembers;
    }
}
