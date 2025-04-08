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
import org.springframework.stereotype.Service;

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



    // 댓글 수정 =========================================================


    // 댓글 석제 =========================================================

}
