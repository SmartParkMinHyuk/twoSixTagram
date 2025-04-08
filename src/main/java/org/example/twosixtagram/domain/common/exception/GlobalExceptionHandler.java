package org.example.twosixtagram.domain.common.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 예시) 아이디가 중복 되었을 때,
     * {
     *  "error": "Invalid Request",
     *  "message": "이메일이 중복되었습니다. 다른 이메일을 사용해주세요.",
     *  "timestamp": "2025-04-08T20:02:34.2187844",
     *  "status": 500
     * }
     **/
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("error", "Duplicate Value");
        errorResponse.put("message", "이메일이 중복되었습니다. 다른 이메일을 사용해주세요.");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    /**
    * 예시) 비밀번호 틀렸을 때, 401
    * {
    *  "error": "Invalid Request",
    *  "message": "비밀번호가 일치하지 않습니다.",
    *  "timestamp": "2025-04-08T18:49:41.5291137",
    *  "status": 401
    * }
    **/
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.UNAUTHORIZED.value());
        error.put("error", "Invalid Request");
        error.put("message", ex.getMessage());
        return error;
    }

    /**
     * 예시) 에러 명시
     * {
     *  "error": "Invalid Request",
     *  "message": "Does not exist id = 5",
     *  "timestamp": "2025-04-08T18:33:50.5752995",
     *  "status": 404
     * }
     **/
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", ex.getStatusCode().value());
        errorResponse.put("error", "Invalid Request");
        errorResponse.put("message", ex.getReason());

        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }


    /**
     * Vaild 에러 명시
     * {
     *  "error": "Invalid Request",
     *  "message": "email required",
     *  "timestamp": "2025-04-08T18:33:30.9768155",
     *  "status": 400
     * }
     **/
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, Object> errorResponse = new HashMap<>();

        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", "Invalid Request");

        List<String> errorMessages = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        errorResponse.put("message", errorMessages.isEmpty() ? "Validation failed" : errorMessages.get(0));

        return errorResponse;
    }

}
