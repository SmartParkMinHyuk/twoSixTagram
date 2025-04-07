package org.example.twosixtagram.domain.user.entity;

import jakarta.persistence.*;
import org.example.twosixtagram.domain.user.FriendStatus;

@Entity

public class User {
    @Id
    private Long id;
    private String email;
    private String password;
    private String name;
    private String mbti; //ENUM으로 처리 가능
    private String idNum; //주민번호앞자리

    @Enumerated(EnumType.STRING)
    private FriendStatus status; //PENDING, ACCEPT, 탈퇴
}
