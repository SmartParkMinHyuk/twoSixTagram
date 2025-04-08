package org.example.twosixtagram.domain.newsfeed.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.twosixtagram.domain.newsfeed.dto.RequestNewsfeedDTO;
import org.example.twosixtagram.domain.newsfeed.dto.ResponseNewsfeedDTO;
import org.example.twosixtagram.domain.newsfeed.entity.NewsFeed;
import org.example.twosixtagram.domain.newsfeed.service.NewsfeedService;
import org.example.twosixtagram.domain.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/feeds")
public class NewsfeedController {

    private final NewsfeedService newsfeedService;
//    private final UserService userService;

    // 피드 등록
    @PostMapping
    public ResponseEntity<ResponseNewsfeedDTO> write(@Valid @RequestBody RequestNewsfeedDTO requestNewsfeedDTO, HttpServletRequest request){

        //세션
        Long loginUser = (Long) request.getSession(false).getAttribute("loginUser");

        //유저 조회 /** 나중에 가져오기 아님 만들기 **/
//        User user = userService.findByIdOrElseThrow(loginUser);

        //세션Id 같이 넘김
//        ResponseNewsfeedDTO respDTO = newsfeedService.register(requestNewsfeedDTO, user);
//        return ResponseEntity.ok(respDTO);
        return null;
    }
    // 피드 수정 ( 로그인 된 상태)

    // 피드 삭제 (로그인 된 상태)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,HttpServletRequest request){
        //세션
        Long loginUser = (Long)request.getSession().getAttribute("loginUser");

        //삭제
        newsfeedService.deleteNewsFeed(id,loginUser);

        return ResponseEntity.noContent().build();
    }



    // 상세보기
    // 상세보기(댓글 포함)  -> API명세서에 추가하기

    // 전체 조회( 댓글 수 포함)  API명세서에 추가하기
    // 비로그인 전체 조회(제목만 10개씩 조회 페이지네이션 )   API명세서에 추가하기
    // 친구한명 뉴스피드 전체조회 ?


}
