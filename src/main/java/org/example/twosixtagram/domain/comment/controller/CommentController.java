package org.example.twosixtagram.domain.comment.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.twosixtagram.domain.comment.dto.RequestCommentDTO;
import org.example.twosixtagram.domain.comment.dto.ResponseCommentDTO;
import org.example.twosixtagram.domain.comment.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import static org.example.twosixtagram.domain.common.util.SessionUtils.getUserId;


import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/feeds/{feedId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 등록 =================================================================================================
    @PostMapping
    public ResponseEntity<ResponseCommentDTO> create(
            @PathVariable Long feedId,
            @RequestBody @Valid RequestCommentDTO requestCommentDTO,
            HttpServletRequest httpServletRequest
    ) {
        Long userId = getUserId(httpServletRequest);
        ResponseCommentDTO response = commentService.createComment(feedId, userId, requestCommentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // 댓글 전체 목록 조회 =================================================================================================
    @GetMapping
    public ResponseEntity<List<ResponseCommentDTO>> getComments(
            @PathVariable Long feedId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<ResponseCommentDTO> comments = commentService.getCommentsByFeedId(feedId, page, size);
        return ResponseEntity.ok(comments);
    }

    // 댓글 수정 =================================================================================================
    @PatchMapping("/{commentId}")
    public ResponseEntity<ResponseCommentDTO> updateComment(
            @PathVariable Long feedId,
            @PathVariable Long commentId,
            @RequestBody @Valid RequestCommentDTO requestCommentDTO,
            HttpServletRequest httpServletRequest
    ) {
        Long userId = getUserId(httpServletRequest);

        ResponseCommentDTO response = commentService.updateComment(feedId, commentId, requestCommentDTO.getContents(), userId);
        return ResponseEntity.ok(response);
    }

    // 댓글 삭제 =================================================================================================
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long feedId,
            @PathVariable Long commentId,
            HttpServletRequest httpServletRequest
    ) {
        Long userId = getUserId(httpServletRequest);

        commentService.deleteComment(feedId, commentId, userId);
        return ResponseEntity.noContent().build();
    }

}
