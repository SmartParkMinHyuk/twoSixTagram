package org.example.twosixtagram.domain.user.entity;

import jakarta.persistence.*;

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
    private UserStatus status; //PENDING, ACCEPT, 탈퇴
}
