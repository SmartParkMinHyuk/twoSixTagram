package org.example.twosixtagram.domain.friend.service;

import lombok.RequiredArgsConstructor;
import org.example.twosixtagram.domain.friend.dto.response.SaveStatusResponseDto;
import org.example.twosixtagram.domain.friend.entity.Friend;
import org.example.twosixtagram.domain.friend.entity.FriendStatus;
import org.example.twosixtagram.domain.friend.repository.FriendRepository;
import org.example.twosixtagram.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;

    private final UserRepository userRepository;


    @Override
    public SaveStatusResponseDto saveStatus(Long id, Long friendId, FriendStatus Status) {


        return null;
    }
}
