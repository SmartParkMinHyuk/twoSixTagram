package org.example.twosixtagram.domain.newsfeed.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.twosixtagram.domain.newsfeed.entity.NewsFeed;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class ResponseNewsfeedDTO {
    private Long id;
    private String userName;
    private String userEmail;
    private String contents;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Entity -> DTO 변환
    public static ResponseNewsfeedDTO toDTO(NewsFeed newsFeed) {
        return ResponseNewsfeedDTO.builder()
                .id(newsFeed.getId())
                .userName(newsFeed.getUser().getName())
                .userEmail(newsFeed.getUser().getEmail())
                .title(newsFeed.getTitle())
                .contents(newsFeed.getContents())
                .updatedAt(newsFeed.getUpdatedAt())
                .build();
    }
}
