package org.example.twosixtagram.domain.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 공통 예외 코드 및 메시지를 정의하는 Enum
 */
@Getter
public enum ErrorCode {

    // 공통
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "요청 값이 유효하지 않습니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    USER_ALREADY_DELETED(HttpStatus.UNAUTHORIZED, "탈퇴한 유저입니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이메일이 중복되었습니다. 다른 이메일을 사용해주세요."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

    // NewsFeed
    FEED_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    FEED_AUTHOR_MISMATCH(HttpStatus.UNAUTHORIZED, "게시글 작성자만 수정/삭제할 수 있습니다."),

    // Comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    COMMENT_FEED_MISMATCH(HttpStatus.BAD_REQUEST, "피드 정보가 일치하지 않습니다."),
    COMMENT_MODIFY_FORBIDDEN(HttpStatus.UNAUTHORIZED, "수정 권한이 없습니다."),
    COMMENT_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
