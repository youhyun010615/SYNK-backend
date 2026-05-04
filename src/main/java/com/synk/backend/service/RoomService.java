package com.synk.backend.service;

import com.synk.backend.dto.request.RoomCreateRequest;
import com.synk.backend.dto.request.RoomJoinRequest;
import com.synk.backend.dto.response.RoomResponse;
import com.synk.backend.entity.Room;
import com.synk.backend.entity.RoomMember;
import com.synk.backend.entity.User;
import com.synk.backend.repository.RoomMemberRepository;
import com.synk.backend.repository.RoomRepository;
import com.synk.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final UserRepository userRepository;

    @Transactional  // 메서드 안의 작업(DB 저장 등)이 하나라도 실패하면 전체를 취소(Rollback)해서 데이터가 꼬이지 않게 보장
    public RoomResponse createRoom(String userId, RoomCreateRequest request){
        // createRoom : 방을 생성함과 동시에 만든 사람을 그 방의 첫 번쨰 맴버로 등록 하는 과정
        User user = getUser(userId);

        String code = generateUniqueCode(); // 코드 생성: generateUniqueCode()를 호출해 중복되지 않는 6자리 입장 코드 생성

        Room room = Room.builder() // Room.builder()로 방 객체를 만들어 roomRepository에 저장
                .code(code)
                .maxMembers(request.getMaxMembers())
                .build();
        roomRepository.save(room);

        RoomMember roomMember = RoomMember.builder()  // 방을 만든 사람도 맴버이므로 RoomMember 객체를 만들어 저장
                .user(user)
                .room(room)
                .build();
        roomMemberRepository.save(roomMember);

        return new RoomResponse(room);  // 새로 만든 방의 정보를 RoomResponse에 담아 돌려준다.
    }

    @Transactional
    public RoomResponse joinRoom(String userId, RoomJoinRequest request){
        // joinRoom : 입장 코드를 입력해 기존에 있던 방에 들어가는 과정
        User user = getUser(userId);

        Room room = roomRepository.findByCode(request.getCode())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 초대 코드입니다"));
        if (roomMemberRepository.existsByUserIdAndRoomId(user.getId(), room.getId())){  // existsByUserIdAndRoomId를 써서 이미 들어와 있는 유저인지 확인
            throw new IllegalArgumentException("이미 참여한 방입니다");
        }

        if (room.getRoomMembers().size() >= room.getMaxMembers()) {
            throw new IllegalArgumentException("방이 꽉 찼습니다");
        }

        RoomMember roomMember = RoomMember.builder()
                .user(user)
                .room(room)
                .build();
        roomMemberRepository.save(roomMember);

        return new RoomResponse(room);
    }

    public List<RoomResponse> getMyRooms(String userId){
        // 내가 참여하고 있는 방들을 리스트로 보여줌
        User user = getUser(userId);
        return roomMemberRepository.findByUserId(user.getId()) // 내가 속한 '참여 기록'들을 싹 가져온다.
                .stream()
                .map(rm -> new RoomResponse(rm.getRoom()))
                .collect(Collectors.toList());  // 우리가 가져온 것은 RoomMember 리스트, 하지만 우리는 Room 정보가 필요하므로 , 각 참여 기록에서
                                                // 방 정보(rm.getRoom())를 꺼내 RoomResponse로 변환 (map) 합니다.
    }                                           // rm은 우리가 이전에 변수로 선언한적 없음, 대신 이 자리에서 "내가 지금부터 쓸 변수 이름은 rm"이라고 즉석에서 정함

    private User getUser(String userId){
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));
    }

    private String generateUniqueCode(){ // 양어 대문자와 숫자를 섞어서 6자리 무작위 코드를 만든다.
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        String code;
        do{ // 만약 생성한 코드가 이미 DB에 있다면 (existsByCode), 안 쓴 코드가 나올 때 까지 계속 새로 만든다 (중복방지)
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < 6; i++){
                sb.append(chars.charAt(random.nextInt(chars.length())));
            }
            code = sb.toString();
        }while (roomRepository.existsByCode(code));
        return code;
    }
}
