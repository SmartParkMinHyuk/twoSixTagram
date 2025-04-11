package org.example.twosixtagram.domain.newsfeed.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RequestUpdateNewsFeedDTO {
    @NotBlank(message = "제목을 작성해주세요. ")
    private String title;
    @NotBlank(message = "내용을 작성해주세요. ")
    private String contents;

}
