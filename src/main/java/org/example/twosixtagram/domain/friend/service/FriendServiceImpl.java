package org.example.twosixtagram.domain.friend.service;

import lombok.RequiredArgsConstructor;
import org.example.twosixtagram.domain.friend.dto.response.GetStatusResponseDto;
import org.example.twosixtagram.domain.friend.dto.response.SaveStatusResponseDto;
import org.example.twosixtagram.domain.friend.entity.Friend;
import org.example.twosixtagram.domain.friend.entity.FriendStatus;
import org.example.twosixtagram.domain.friend.repository.FriendRepository;
import org.example.twosixtagram.domain.user.entity.User;
import org.example.twosixtagram.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;

    private final UserRepository userRepository;

    @Override
    public SaveStatusResponseDto saveStatus(Long id, Long friendId, FriendStatus status) {
        // userId, friendId가 있는지 확인과 동시에 인스턴스화
        User userOpt = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("유저가 없습니다: "+ id));
        User friendOpt = userRepository.findById(friendId).orElseThrow(
                () -> new IllegalArgumentException("유저가 없습니다: "+ friendId));

        // userId,friendId,status를 Friend 엔티티 타입으로 인스턴스화
        Friend saveStatus = new Friend(userOpt, friendOpt, status);

        friendRepository.save(saveStatus);
        // 프론트에서 friend의 정보를 확인하고
        return new SaveStatusResponseDto(friendOpt.getId(),friendOpt.getEmail(),friendOpt.getName(),status);
    }

    @Override
    public List<GetStatusResponseDto> getStatus(Long friendId) {

        List<Friend> pendingFriend = friendRepository.findByFriend_IdAndStatus(friendId,FriendStatus.PENDING);

        if(pendingFriend.isEmpty()){
            throw new IllegalArgumentException("친구 요청이 없습니다.");
        }

        List<GetStatusResponseDto> dtolist = pendingFriend.stream()
                .map(friend -> new GetStatusResponseDto(
                        friend.getUser().getId(),
                        friend.getUser().getEmail(),
                        friend.getUser().getName()))
                .toList();

        return dtolist ;

    }
}
