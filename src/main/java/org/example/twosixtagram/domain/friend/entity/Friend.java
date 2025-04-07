package org.example.twosixtagram.domain.friend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.twosixtagram.domain.user.entity.User;

@Entity
@Table( name ="friend")
@Builder
@Getter
@AllArgsConstructor
public class Friend {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // 나
        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        // 친구
        @ManyToOne
        @JoinColumn(name = "friend_id")
        private User friend;

        // 요청/수락 상태 (PENDING, ACCEPTED, DECLINED, REMOVED)
        @Enumerated(EnumType.STRING)
        private FriendStatus status;

    }
