package org.example.twosixtagram.domain.friend.dto.request;

import lombok.Getter;
import org.example.twosixtagram.domain.friend.entity.FriendStatus;

@Getter
public class UpdateRequestFriendDto {

    private final FriendStatus status;

    public UpdateRequestFriendDto(FriendStatus status) {
        this.status = status;
    }
}
