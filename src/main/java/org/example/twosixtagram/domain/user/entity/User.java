package org.example.twosixtagram.domain.user.entity;

import jakarta.persistence.*;

@Entity

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private MBTI mbti;

    @Column(name = "created_at", nullable = false, length = 6)
    private String idNum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    public void changeStatus(UserStatus status) {
        this.status = status;
    }
}
