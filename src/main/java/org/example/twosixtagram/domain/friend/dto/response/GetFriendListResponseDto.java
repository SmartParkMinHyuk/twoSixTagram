package org.example.twosixtagram.domain.friend.dto.response;

import lombok.Getter;

@Getter
public class GetFriendListResponseDto {
    private final Long id;

    private final String email;

    private final String name;

    public GetFriendListResponseDto(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}
