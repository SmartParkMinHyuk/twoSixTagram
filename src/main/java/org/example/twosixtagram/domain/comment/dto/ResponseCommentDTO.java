package org.example.twosixtagram.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ResponseCommentDTO {
    private Long id;
    private String contents;
    private Long userId;
    private String userName;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
