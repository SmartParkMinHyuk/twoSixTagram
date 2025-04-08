package org.example.twosixtagram.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.twosixtagram.domain.comment.dto.RequestCommentDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CommentApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

//    @Test
//    void createComment throws Exception {
//
//        // given
//        RequestCommentDTO requestCommentDTO = new RequestCommentDTO();
//
//    }

}

// SpringBootTest
// : @Component, @Service, @Repository 모두 적용된 상태로 테스트됨
// : 실제 애플리케이션처럼 의존성 주입, 트랜잭션, 설정파일 로딩 등이 작동

// MockMvc
// : 웹 요청을 실제 서버 띄우지 않고 시뮬레이션할 수 있는 유틸

// ObjectMapper
// : JSON 직렬화/역직렬화 도와주는 Jackson 도구

// @Transactional
// : 테스트 메서드 실행 후, 자동으로 롤백시켜 데이터베이스를 원상복구