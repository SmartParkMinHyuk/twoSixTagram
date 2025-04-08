package org.example.twosixtagram.domain.newsfeed.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.twosixtagram.domain.comment.entity.Comment;
import org.example.twosixtagram.domain.common.auditing.BaseEntity;
import org.example.twosixtagram.domain.user.entity.User;

@Entity
@Table(name = "newsfeed")
@Getter
@Builder
@AllArgsConstructor
public class NewsFeed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "longtext")
    private String contents;

    @Column(nullable = false)
    private Long userid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id",nullable = false)
    private Comment comment;

    public NewsFeed() {
    }


}
