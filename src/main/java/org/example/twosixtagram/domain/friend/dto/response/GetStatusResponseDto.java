package org.example.twosixtagram.domain.friend.dto.response;

import lombok.Getter;

@Getter
public class GetStatusResponseDto {

    private final Long userId;

    private final String email;

    private final String name;

    public GetStatusResponseDto(Long userId, String email, String name) {
        this.userId = userId;
        this.email = email;
        this.name = name;
    }
}
