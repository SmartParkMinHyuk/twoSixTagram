package org.example.twosixtagram.domain.friend.service;

import org.example.twosixtagram.domain.friend.dto.response.*;
import org.example.twosixtagram.domain.friend.entity.FriendStatus;

import java.util.List;

public interface FriendService {
    SaveStatusResponseDto saveStatus(Long id,Long friendId,FriendStatus status);

    List<GetStatusResponseDto> getStatus(Long friendId);

    AcceptStatusResponseDto acceptStatus(Long friendId,Long userId,FriendStatus status);

    List<GetFriendListResponseDto> getFriendList(Long id);

    void deleteFriend(Long id);

    GetProfileResponseDto getProfile(Long id);
}
