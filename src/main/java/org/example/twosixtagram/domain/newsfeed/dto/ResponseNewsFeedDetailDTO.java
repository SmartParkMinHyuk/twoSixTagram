package org.example.twosixtagram.domain.newsfeed.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.twosixtagram.domain.comment.entity.Comment;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Builder
@Getter
public class ResponseNewsFeedDetailDTO {

    private Long feedId;
    private String title;
    private String contents;
    private LocalDateTime modifiedAt;

    private String userName;   // 작성자 이름
    private String userEmail;  // 작성자 이메일

    //댓글 목록 (무한스크롤 아니면 수정) 댓글 되면 수정하기**/
    Slice<Comment> commentList;
}
