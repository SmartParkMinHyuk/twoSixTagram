package org.example.twosixtagram.domain.comment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import static org.example.twosixtagram.domain.common.exception.ErrorCode.*;
import org.example.twosixtagram.domain.comment.dto.RequestCommentDTO;
import org.example.twosixtagram.domain.comment.dto.ResponseCommentDTO;
import org.example.twosixtagram.domain.comment.entity.Comment;
import org.example.twosixtagram.domain.comment.repository.CommentRepository;
import org.example.twosixtagram.domain.common.exception.ErrorCode;
import org.example.twosixtagram.domain.newsfeed.entity.NewsFeed;
import org.example.twosixtagram.domain.newsfeed.repository.NewsfeedRepository;
import org.example.twosixtagram.domain.user.entity.User;
import org.example.twosixtagram.domain.user.entity.UserStatus;
import org.example.twosixtagram.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final NewsfeedRepository newsfeedRepository;
    private final UserRepository userRepository;

    // 댓글 등록 ===================================================================================================
    public ResponseCommentDTO createComment(Long feedId, Long userId, RequestCommentDTO requestCommentDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> exception(USER_NOT_FOUND));

        // 사용자 조회 및 탈퇴 여부 확인
        throwIfUnauthorized(user.getStatus() == null || user.getStatus() == UserStatus.UNACTIVE, USER_ALREADY_DELETED);

        // 피드 존재 확인
        // 1. 보안 + 무결성 보장 (프론트에서 조작 방지)
        // 2. 댓글-피드 연결을 위해 실제 Entity 필요
        // 3. JPA Cascade & 연관관계 기능 활용 (ex. 피드 삭제 시 댓글도 함께 삭제)
        NewsFeed feed = newsfeedRepository.findById(feedId)
                .orElseThrow(() -> exception(FEED_NOT_FOUND));

        // 댓글 생성 + 저장
        // feed: feedId X --> JPA 관계 매핑을 위한 실제 엔티티 인스턴스.
        Comment comment = new Comment(user, feed, requestCommentDTO.getContents());
        Comment savedComment = commentRepository.save(comment);

        return ResponseCommentDTO.builder()
                .id(savedComment.getId())
                .contents(savedComment.getContents())
                .userId(user.getId())
                .userName(comment.getUser().getName())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getUpdatedAt())
                .build();
    }

    // 댓글 전체 목록 조회 =================================================================================================
    public List<ResponseCommentDTO> getCommentsByFeedId(Long feedId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findByNewsFeedId(feedId, pageRequest);

        // 존재하지 않는 피드일 경우 예외처리
        if (!newsfeedRepository.existsById(feedId)) {
            throw exception(FEED_NOT_FOUND);
        }

        return commentPage.getContent().stream()
                .map(comment -> ResponseCommentDTO.builder()
                        .id(comment.getId())
                        .contents(comment.getContents())
                        .userId(comment.getUser().getId())
                        .userName(comment.getUser().getName())
                        .createdAt(comment.getCreatedAt())
                        .modifiedAt(comment.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }


    // 댓글 수정 =================================================================================================
    public ResponseCommentDTO updateComment(Long feedId, Long commentId, String newContent, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> exception(COMMENT_NOT_FOUND));

        // 피드 존재 확인
        throwIfUnauthorized(!comment.getNewsFeed().getId().equals(feedId), COMMENT_FEED_MISMATCH);

        Long commentAuthorId = comment.getUser().getId();
        Long feedAuthorId = comment.getNewsFeed().getUser().getId();

        // 수정 권한 검증
        throwIfUnauthorized(!userId.equals(commentAuthorId) && !userId.equals(feedAuthorId), COMMENT_MODIFY_FORBIDDEN);

        comment.updateContent(newContent);

        return ResponseCommentDTO.builder()
                .id(comment.getId())
                .contents(comment.getContents())
                .userId(comment.getUser().getId())
                .userName(comment.getUser().getName())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getUpdatedAt())
                .build();
    }

    // 댓글 삭제 =================================================================================================
    public void deleteComment(Long feedId, Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> exception(COMMENT_NOT_FOUND));

        throwIfUnauthorized(!comment.getNewsFeed().getId().equals(feedId), COMMENT_FEED_MISMATCH);

        Long commentAuthorId = comment.getUser().getId();
        Long feedAuthorId = comment.getNewsFeed().getUser().getId();

        // 삭제 권한 검증
        throwIfUnauthorized(!userId.equals(commentAuthorId) && !userId.equals(feedAuthorId), COMMENT_DELETE_FORBIDDEN);

        commentRepository.delete(comment);
    }

    // 공통 ============================================================================================
    private void throwIfUnauthorized(boolean condition, ErrorCode errorCode) {
        if (condition) {
            throw exception(errorCode);
        }
    }

    private ResponseStatusException exception(ErrorCode errorCode) {
        return new ResponseStatusException(errorCode.getStatus(), errorCode.getMessage());
    }
}
