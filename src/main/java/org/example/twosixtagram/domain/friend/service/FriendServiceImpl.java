package org.example.twosixtagram.domain.friend.service;

import lombok.RequiredArgsConstructor;
import org.example.twosixtagram.domain.friend.dto.response.SaveStatusResponseDto;
import org.example.twosixtagram.domain.friend.entity.Friend;
import org.example.twosixtagram.domain.friend.entity.FriendStatus;
import org.example.twosixtagram.domain.friend.repository.FriendRepository;
import org.example.twosixtagram.domain.user.entity.User;
import org.example.twosixtagram.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;

    private final UserRepository userRepository;


    @Override
    public SaveStatusResponseDto saveStatus(Long id, Long friendId, FriendStatus status) {
        User userOpt = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("유저가 없습니다: "+ id));
        User friendOpt = userRepository.findById(friendId).orElseThrow(
                () -> new IllegalArgumentException("유저가 없습니다: "+ friendId));


        Friend saveStatus = new Friend(userOpt, friendOpt, status);

        friendRepository.save(saveStatus);

        return new SaveStatusResponseDto(friendOpt.getId(),friendOpt.getEmail(),friendOpt.getName());
    }
}
