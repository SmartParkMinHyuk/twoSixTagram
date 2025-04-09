package org.example.twosixtagram.domain.friend.dto.request;

import lombok.Getter;
import org.example.twosixtagram.domain.friend.entity.FriendStatus;

@Getter
public class AcceptStatusRequestDto {
    private final Long userId;

    private final FriendStatus status;

    public AcceptStatusRequestDto(Long userId, FriendStatus status) {
        this.userId = userId;
        this.status = status;
    }
}
