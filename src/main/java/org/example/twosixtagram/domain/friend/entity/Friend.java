package org.example.twosixtagram.domain.friend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import lombok.NoArgsConstructor;
import org.example.twosixtagram.domain.common.auditing.BaseEntity;
import org.example.twosixtagram.domain.user.entity.User;

@Entity
@Table( name ="friend")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Friend extends BaseEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // 요청자
        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        // 요청받는사람
        @ManyToOne
        @JoinColumn(name = "friend_id")
        private User friend;

        // 요청/수락 상태 (PENDING, ACCEPTED, DECLINED, REMOVED)
        @Enumerated(EnumType.STRING)
        private FriendStatus status;


        public Friend(User user, User friend, FriendStatus status) {
                this.user = user;
                this.friend = friend;
                this.status = status;
        }

        public void updateStatus(FriendStatus status){
                this.status = status;
        }
}
