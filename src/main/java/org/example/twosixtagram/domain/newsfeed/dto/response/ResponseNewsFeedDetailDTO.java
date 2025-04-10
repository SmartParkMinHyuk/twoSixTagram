package org.example.twosixtagram.domain.newsfeed.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.twosixtagram.domain.comment.dto.ResponseCommentDTO;
import org.example.twosixtagram.domain.newsfeed.entity.NewsFeed;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@Getter
public class ResponseNewsFeedDetailDTO {

    private Long id;
    private String title;
    private String contents;
    private LocalDateTime updatedAt;

    private String userName;   // 작성자 이름
    private String userEmail;  // 작성자 이메일

    //댓글 목록 (무한스크롤 아니면 수정) 댓글 되면 수정하기**/
    //Slice<Comment> commentList;
    private List<ResponseCommentDTO> comments;

    //다음 페이지 여부
    private boolean hasNext;

    public static ResponseNewsFeedDetailDTO toDTO(NewsFeed feed,List<ResponseCommentDTO> comments,boolean hasNext ){
        return ResponseNewsFeedDetailDTO.builder()
                .id(feed.getId())
                .title(feed.getTitle())
                .contents(feed.getContents())
                .updatedAt(feed.getUpdatedAt())
                .userName(feed.getUser().getName())
                .userEmail(feed.getUser().getEmail())
                .comments(comments)
                .hasNext(hasNext)
                .build();
    }


}
