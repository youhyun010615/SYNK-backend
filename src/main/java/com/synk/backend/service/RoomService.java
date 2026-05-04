package com.synk.backend.service;

import com.synk.backend.dto.request.RoomCreateRequest;
import com.synk.backend.dto.request.RoomJoinRequest;
import com.synk.backend.dto.response.RoomMemberResponse;
import com.synk.backend.dto.response.RoomResponse;
import com.synk.backend.entity.Room;
import com.synk.backend.entity.RoomMember;
import com.synk.backend.entity.User;
import com.synk.backend.repository.RoomMemberRepository;
import com.synk.backend.repository.RoomRepository;
import com.synk.backend.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public RoomResponse createRoom(String userId, RoomCreateRequest request) {
        User user = getUser(userId);
        String code = generateUniqueCode();

        Room savedRoom = roomRepository.save(Room.builder()
                .code(code)
                .maxMembers(request.getMaxMembers())
                .build());

        roomMemberRepository.saveAndFlush(RoomMember.builder()
                .user(user)
                .room(savedRoom)
                .build());

        int count = roomMemberRepository.countByRoom(savedRoom);
        return new RoomResponse(savedRoom, count);
    }

    @Transactional
    public RoomResponse joinRoom(String userId, RoomJoinRequest request) {
        User user = getUser(userId);

        Room room = roomRepository.findByCode(request.getCode())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 초대 코드입니다"));

        if (roomMemberRepository.existsByUserAndRoom(user, room)) {
            throw new IllegalArgumentException("이미 참여한 방입니다");
        }

        if (roomMemberRepository.countByRoom(room) >= room.getMaxMembers()) {
            throw new IllegalArgumentException("방이 꽉 찼습니다");
        }

        roomMemberRepository.saveAndFlush(RoomMember.builder()
                .user(user)
                .room(room)
                .build());

        int count = roomMemberRepository.countByRoom(room);
        return new RoomResponse(room, count);
    }

    public List<RoomResponse> getMyRooms(String userId) {
        User user = getUser(userId);
        return roomMemberRepository.findByUser(user)
                .stream()
                .map(rm -> new RoomResponse(rm.getRoom(), roomMemberRepository.countByRoom(rm.getRoom())))
                .collect(Collectors.toList());
    }

    public List<RoomMemberResponse> getRoomMembers(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다"));
        return roomMemberRepository.findByRoom(room)
                .stream()
                .map(RoomMemberResponse::new)
                .collect(Collectors.toList());
    }

    private User getUser(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));
    }

    private String generateUniqueCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        String code;
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                sb.append(chars.charAt(random.nextInt(chars.length())));
            }
            code = sb.toString();
        } while (roomRepository.existsByCode(code));
        return code;
    }
}