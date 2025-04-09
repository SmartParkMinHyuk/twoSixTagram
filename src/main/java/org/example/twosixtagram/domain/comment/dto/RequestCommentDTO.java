package org.example.twosixtagram.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RequestCommentDTO {

    private Long userId;

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String contents;

}
