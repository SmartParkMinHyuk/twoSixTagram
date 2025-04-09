package org.example.twosixtagram.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseCommentDTO {
    private Long id;
    private String contents;
    private Long userId;
    private Long feedId;
}
