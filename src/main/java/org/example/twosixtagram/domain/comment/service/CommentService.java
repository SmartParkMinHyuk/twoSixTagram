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
import org.example.twosixtagram.domain.user.entity.UserStatus;
import org.example.twosixtagram.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        // 사용자 조회 및 탈퇴 여부 확인
        if (user.getStatus() == null || user.getStatus() == UserStatus.UNACTIVE) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "탈퇴한 유저입니다.");
        }

        // 피드 존재 확인
        // 1. 보안 + 무결성 보장 (프론트에서 조작 방지)
        // 2. 댓글-피드 연결을 위해 실제 Entity 필요
        // 3. JPA Cascade & 연관관계 기능 활용 (ex. 피드 삭제 시 댓글도 함께 삭제)
        NewsFeed feed = newsfeedRepository.findById(feedId)
                .orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        // 댓글 생성 + 저장
        // feed: feedId X --> JPA 관계 매핑을 위한 실제 엔티티 인스턴스.
        Comment comment = new Comment(user, feed, requestCommentDTO.getContents());
        Comment savedComment = commentRepository.save(comment);

        return ResponseCommentDTO.builder()
                .id(savedComment.getId())
                .contents(savedComment.getContents())
                .userId(user.getId())
                .feedId(feed.getId())
                .build();
    }

    // 댓글 전체 목록 조회 =================================================================================================
    public List<ResponseCommentDTO> getCommentsByFeedId(Long feedId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findByNewsFeedId(feedId, pageRequest);

        return commentPage.getContent().stream()
                .map(comment -> ResponseCommentDTO.builder()
                        .id(comment.getId())
                        .contents(comment.getContents())
                        .userId(comment.getUser().getId())
                        .feedId(comment.getNewsFeed().getId())
                        .build())
                .collect(Collectors.toList());
    }


    // 댓글 수정 =================================================================================================
    public ResponseCommentDTO updateComment(Long feedId, Long commentId, String newContent, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글 없음"));

        if (!comment.getNewsFeed().getId().equals(feedId)) {
            throw new RuntimeException("피드 정보가 일치하지 않음");
        }

        Long commentAuthorId = comment.getUser().getId();
        Long feedAuthorId = comment.getNewsFeed().getUser().getId();

        if (!userId.equals(commentAuthorId) && !userId.equals(feedAuthorId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "수정 권한이 없습니다.");
        }

        comment.updateContent(newContent);

        return ResponseCommentDTO.builder()
                .id(comment.getId())
                .contents(comment.getContents())
                .userId(comment.getUser().getId())
                .feedId(comment.getNewsFeed().getId())
                .build();
    }

    // 댓글 삭제 =================================================================================================
    public void deleteComment(Long feedId, Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글 없음"));

        if (!comment.getNewsFeed().getId().equals(feedId)) {
            throw new RuntimeException("피드 정보가 일치하지 않음");
        }

        Long commentAuthorId = comment.getUser().getId();
        Long feedAuthorId = comment.getNewsFeed().getUser().getId();

        if (!userId.equals(commentAuthorId) && !userId.equals(feedAuthorId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}
