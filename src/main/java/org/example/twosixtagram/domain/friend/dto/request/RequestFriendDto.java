package org.example.twosixtagram.domain.friend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.example.twosixtagram.domain.friend.entity.FriendStatus;

@Getter
public class RequestFriendDto {

    @NotBlank
    private final Long friendId;

    @NotBlank
    private final FriendStatus status;


    public RequestFriendDto(Long friendId, FriendStatus status) {
        this.friendId = friendId;
        this.status = status;
    }

}
