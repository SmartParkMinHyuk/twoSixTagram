package org.example.twosixtagram.domain.newsfeed.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.twosixtagram.domain.newsfeed.dto.*;
import org.example.twosixtagram.domain.newsfeed.service.NewsfeedService;
import org.example.twosixtagram.domain.user.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/feeds")
public class NewsfeedController {

    private final NewsfeedService newsfeedService;

    // 피드 등록
    @PostMapping
    public ResponseEntity<ResponseNewsfeedDTO> write(@Valid @RequestBody RequestNewsfeedDTO requestNewsfeedDTO, HttpServletRequest request){
        //세션
        Long loginUser = (Long) request.getSession(false).getAttribute("userId");
        log.info("피드등록 ");
        log.info(loginUser.toString());
        //세션Id 같이 넘김
        ResponseNewsfeedDTO respDTO = newsfeedService.register(requestNewsfeedDTO, loginUser);
        return ResponseEntity.ok(respDTO);
    }
    // 피드 수정 ( 로그인 된 상태)
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseUpdateNewsFeedDTO> update(@PathVariable Long id, @RequestBody RequestUpdateNewsFeedDTO reqDTO , HttpServletRequest request){
        Long loginUser = (Long)request.getSession(false).getAttribute("userId");

        ResponseUpdateNewsFeedDTO respDTO = newsfeedService.updateFeed(id,reqDTO,loginUser);

        return ResponseEntity.ok(respDTO);
    }
    // 피드 삭제 (로그인 된 상태)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,HttpServletRequest request){
        //세션
        Long loginUser = (Long)request.getSession().getAttribute("userId");

        //삭제
        newsfeedService.deleteNewsFeed(id,loginUser);

        return ResponseEntity.noContent().build();
    }


    // 상세보기(댓글 포함)  -> 게시글 하나와 페이징 처리된 댓글 조회
    @GetMapping("/{id}")
    public ResponseEntity<ResponseNewsFeedDetailDTO> read(
            @PathVariable Long id,
            @PageableDefault(page = 0, size = 10, sort = "modifiedAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        //로그인필터 걸러서 온다음
        //상세보기에서 수정버튼 누를시 유저 검증은 수정에서 하니 필요없나 ?
        //상세보기시 삭제, 수정 -> 프론트에서 ? , 그냥 보여주기만 해도되려는지 (튜터님한테 질문하기)

        log.info("게시물 상세보기 ==" );
        ResponseNewsFeedDetailDTO respDTO = newsfeedService.getFeed(id, pageable);
        return ResponseEntity.ok(respDTO);
    }
    // 전체 조회( 댓글 수 포함)  API명세서에 추가하기
//    public ResponseEntity<ResponseNewsFeed>
    // 비로그인 전체 조회(제목만 10개씩 조회 페이지네이션 )   API명세서에 추가하기

    // 친구들의 뉴스피드 전체조회

    // 친구한명 뉴스피드 전체조회


}
