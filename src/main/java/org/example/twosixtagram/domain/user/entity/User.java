package org.example.twosixtagram.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.twosixtagram.domain.common.auditing.BaseEntity;
import org.example.twosixtagram.domain.user.dto.UserSignupRequest;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private UserStatus status;

    @Builder
    public User(String email, String password, String name, MBTI mbti, String idNum, UserStatus status) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.mbti = mbti;
        this.idNum = idNum;
        this.status = status;
    }

    public static User create(UserSignupRequest request) {
        return User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .mbti(request.getMbti())
                .idNum(request.getIdNum())
                .status(UserStatus.ACTIVE)
                .build();
    }

    public void userRemove(UserStatus status) {
        this.status = status;
    }
}
