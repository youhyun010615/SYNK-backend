package com.synk.backend.service;

import com.synk.backend.dto.request.MissionCreateRequest;
import com.synk.backend.dto.response.MissionResponse;
import com.synk.backend.entity.Mission;
import com.synk.backend.entity.Room;
import com.synk.backend.entity.enums.MissionStatus;
import com.synk.backend.repository.MissionRepository;
import com.synk.backend.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public MissionResponse createMission(MissionCreateRequest request){
        // 1. 어느 방에서 만드는지 확인
        Room room = roomRepository.findById(request.getRoomId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다"));

        // 2. 미션 조립 (빌더 패턴)
        Mission mission = Mission.builder()
                .room(room)
                .missionType(request.getMissionType())
                .targetedAt(LocalDateTime.now()) // 지금 즉시 시작
                .build();

        // 3. DB 저장 및 결과 반환
        return new MissionResponse(missionRepository.save(mission));
    }

    // 보통 게임이나 채티방에서 "지금 미션이 떴어요!" 라고 화면에 뿌려줄 때 이 메서드 호출
    public List<MissionResponse> getActiveMissions(Long roomId){
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다"));// 사용자가 보내준 roomID로 실제 방이 DB에 있는지 먼저 확인

        return missionRepository.findByRoomAndMissionStatus(room, MissionStatus.ACTIVE)
                // MissionRepository에게 두가지 조건을 던짐 1.이 방(room)에 있는지, 2.미션 상태가 현재 진행 중(ACTIVE)인지
                .stream() // 찾은 미션 리스트를 하나씩 처리하기 위해 컨베이어 벨트에 올림
                .map(MissionResponse::new)// 이 표현식은 map(m -> new MissionResponse(m))과 똑같은 의미
                .collect(Collectors.toList());
    }
}
