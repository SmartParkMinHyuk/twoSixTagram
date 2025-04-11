package org.example.twosixtagram.domain.newsfeed.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.twosixtagram.domain.newsfeed.entity.NewsFeed;

import java.time.LocalDateTime;

@Getter
@Builder
public class ResponseNewsFeedListDTO {
    private Long id;
    private String title;
    private String contents;
    private String userName;
    private String userEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

//다음 페이지 여부
//        private Long commentCount; //게시글에 달린 댓글 수

    public static ResponseNewsFeedListDTO toDTO(NewsFeed feed) {
        return ResponseNewsFeedListDTO.builder()
                .id(feed.getId())
                .title(feed.getTitle())
                .contents(feed.getContents())
                .userName(feed.getUser().getName())
                .userEmail(feed.getUser().getEmail())
//                .commentCount(commentCount)  // 게시글에 달린 댓글 수
                .createdAt(feed.getCreatedAt())
                .updatedAt(feed.getUpdatedAt())
                .build();
    }
}
