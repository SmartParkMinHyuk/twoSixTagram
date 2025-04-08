package org.example.twosixtagram.domain.user.dto;

import lombok.Getter;
import org.example.twosixtagram.domain.user.entity.MBTI;
import org.example.twosixtagram.domain.user.entity.User;

@Getter
public class UserResponse {

    private final Long id;
    private final String email;
    private final String name;
    private final MBTI mbti;
    private final String idNum;

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.mbti = user.getMbti();
        this.idNum = user.getIdNum();
    }

}
