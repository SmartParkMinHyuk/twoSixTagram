package org.example.twosixtagram.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.twosixtagram.domain.comment.dto.RequestCommentDTO;
import org.example.twosixtagram.domain.comment.dto.ResponseCommentDTO;
import org.example.twosixtagram.domain.comment.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/feeds/{feedId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 등록 =========================================================
    @PostMapping
    public ResponseEntity<ResponseCommentDTO> create(
            @PathVariable Long feedId,
            @RequestBody @Valid RequestCommentDTO requestCommentDTO
    ) {
        ResponseCommentDTO response = commentService.writeComment(feedId, requestCommentDTO);
        return ResponseEntity.ok(response);
    }


    // 댓글 전체 목록 조회 =========================================================
    @GetMapping
    public ResponseEntity<List<ResponseCommentDTO>> getComments(@PathVariable Long feedId) {
//        return ResponseEntity.ok(commentService.getCommentByFeed(feedId));
        return null;
    }


    // 댓글 수정 =========================================================


    // 댓글 석제 =========================================================


}
