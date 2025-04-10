package org.example.twosixtagram.domain.friend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.example.twosixtagram.domain.friend.entity.FriendStatus;

@Getter
public class RequestFriendDto {

    private final Long friendId;

    private final FriendStatus status;


    public RequestFriendDto(Long friendId, FriendStatus status) {
        this.friendId = friendId;
        this.status = status;
    }

}
