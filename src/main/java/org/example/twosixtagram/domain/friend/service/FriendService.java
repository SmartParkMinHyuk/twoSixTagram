package org.example.twosixtagram.domain.friend.service;

import jakarta.validation.constraints.NotBlank;
import org.example.twosixtagram.domain.friend.dto.response.GetStatusResponseDto;
import org.example.twosixtagram.domain.friend.dto.response.SaveStatusResponseDto;
import org.example.twosixtagram.domain.friend.entity.FriendStatus;

import java.util.List;

public interface FriendService {
    SaveStatusResponseDto saveStatus(Long id,Long friendId,FriendStatus status);

    List<GetStatusResponseDto> getStatus(Long friendId);
}
