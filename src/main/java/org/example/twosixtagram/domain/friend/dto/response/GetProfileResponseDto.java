package org.example.twosixtagram.domain.friend.dto.response;

import lombok.Getter;
import org.example.twosixtagram.domain.user.entity.MBTI;

@Getter
public class GetProfileResponseDto {

    private final Long id;

    private final String name;

    private final String email;

    private final MBTI mbti;

    public GetProfileResponseDto(Long id, String name, String email, MBTI mbti) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mbti = mbti;
    }
}
