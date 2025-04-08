package org.example.twosixtagram.domain.newsfeed.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.twosixtagram.domain.newsfeed.entity.NewsFeed;
import org.example.twosixtagram.domain.user.entity.User;

@AllArgsConstructor
@Getter
public class RequestNewsfeedDTO {
    //valid처리
    @NotBlank(message = "제목을 입력 해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력 해주세요.")
    private String contents;

    //toEntity
    public  NewsFeed toEntity(User user){
        return NewsFeed.builder()
                .title(this.title)
                .contents(this.contents)
                .user(user)
                .build();
    }
}


