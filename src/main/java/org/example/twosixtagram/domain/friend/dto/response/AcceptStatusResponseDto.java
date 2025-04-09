package org.example.twosixtagram.domain.friend.dto.response;

import lombok.Getter;
import org.example.twosixtagram.domain.friend.entity.FriendStatus;

@Getter
public class AcceptStatusResponseDto {

    private final FriendStatus status;

    public AcceptStatusResponseDto(FriendStatus status) {
        this.status = status;
    }
}
