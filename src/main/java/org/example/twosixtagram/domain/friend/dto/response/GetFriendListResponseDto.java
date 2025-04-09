package org.example.twosixtagram.domain.friend.dto.response;

import lombok.Getter;

@Getter
public class GetFriendListResponseDto {
    private final String email;

    private final String name;

    public GetFriendListResponseDto(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
