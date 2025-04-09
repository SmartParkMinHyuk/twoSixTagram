package org.example.twosixtagram.domain.friend.dto.response;

import lombok.Getter;
import org.example.twosixtagram.domain.friend.entity.FriendStatus;

@Getter
public class SaveStatusResponseDto {
    private final Long id;

    private final String email;

    private final String name;

    private final FriendStatus status;

    public SaveStatusResponseDto(Long id, String email, String name, FriendStatus status) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.status = status;
    }
}
