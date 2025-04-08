package org.example.twosixtagram.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.twosixtagram.domain.common.auditing.BaseEntity;

@Entity
@Table(name = "user")
@Getter
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private MBTI mbti;

    @Column(name = "id_num", nullable = false, length = 6)
    private String idNum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    public User() {
    }

    public void changeStatus(UserStatus status) {
        this.status = status;
    }
}
