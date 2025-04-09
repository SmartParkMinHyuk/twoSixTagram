package org.example.twosixtagram.domain.comment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.twosixtagram.domain.comment.dto.RequestCommentDTO;
import org.example.twosixtagram.domain.comment.dto.ResponseCommentDTO;
import org.example.twosixtagram.domain.comment.entity.Comment;
import org.example.twosixtagram.domain.comment.repository.CommentRepository;
import org.example.twosixtagram.domain.newsfeed.entity.NewsFeed;
import org.example.twosixtagram.domain.newsfeed.repository.NewsfeedRepository;
import org.example.twosixtagram.domain.user.entity.User;
import org.example.twosixtagram.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final NewsfeedRepository newsfeedRepository;
    private final UserRepository userRepository;

    // 댓글 등록 =========================================================
    public ResponseCommentDTO writeComment(Long feedId, RequestCommentDTO requestCommentDTO) {
        User user = userRepository.findById(requestCommentDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        NewsFeed feed = newsfeedRepository.findById(requestCommentDTO.getNewsFeedId())
                .orElseThrow(() -> new RuntimeException("피드 없음"));

        Comment comment = new Comment(user, feed, requestCommentDTO.getContent());
        commentRepository.save(comment);

        return ResponseCommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContents())
                .userId(user.getId())
                .newsFeedId(feed.getId())
                .build();
    }

    // 댓글 전체 목록 조회 =========================================================
    public List<ResponseCommentDTO> getCommentsByFeed(Long feedId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findByNewsFeedId(feedId, pageRequest);

        return commentPage.getContent().stream()
                .map(comment -> ResponseCommentDTO.builder()
                        .id(comment.getId())
                        .content(comment.getContents())
                        .userId(comment.getUser().getId())
                        .newsFeedId(comment.getNewsFeed().getId())
                        .build())
                .collect(Collectors.toList());
    }


    // 댓글 수정 =========================================================
    public ResponseCommentDTO updateComment(Long feedId, Long commentId, String newContent) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글 없음"));

        if (!comment.getNewsFeed().getId().equals(feedId)) {
            throw new RuntimeException("피드 정보가 일치하지 않음");
        }

        comment.updateContent(newContent);

        return ResponseCommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContents())
                .userId(comment.getUser().getId())
                .newsFeedId(comment.getNewsFeed().getId())
                .build();
    }

    // 댓글 삭제 =========================================================
    public void deleteComment(Long feedId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글 없음"));

        if (!comment.getNewsFeed().getId().equals(feedId)) {
            throw new RuntimeException("피드 정보가 일치하지 않음");
        }

        commentRepository.delete(comment);
    }
}
