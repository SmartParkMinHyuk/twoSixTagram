package org.example.twosixtagram.domain.newsfeed.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.twosixtagram.domain.newsfeed.dto.request.RequestNewsfeedDTO;
import org.example.twosixtagram.domain.newsfeed.dto.request.RequestUpdateNewsFeedDTO;
import org.example.twosixtagram.domain.newsfeed.dto.response.*;
import org.example.twosixtagram.domain.newsfeed.service.NewsfeedService;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //피드 상세보기 & 댓글리스트 포함 (hasNext로 무한스크롤)
    @GetMapping("/{id}")
    public ResponseEntity<ResponseNewsFeedDetailDTO> read(
            @PathVariable Long id,
            @PageableDefault(page = 0, size = 10, sort = "updatedAt", direction = Sort.Direction.ASC) //댓글 최신이 밑으로 가게
            Pageable pageable, HttpServletRequest request) {

        ResponseNewsFeedDetailDTO respDTO = newsfeedService.getFeed(id, pageable);
        return ResponseEntity.ok(respDTO);
    }

//   피드 전체조회(무한스크롤) + 댓글수
    @GetMapping
    public ResponseEntity<Slice<ResponseNewsFeedListCommentCountDTO>> getListWithCommentCount(
            @RequestParam(value = "page", defaultValue = "1") int clientPage,HttpServletRequest request){

        int row = 10;
        Pageable pageable = PageRequest.of(clientPage-1,row,Sort.by("updatedAt").descending());
        Slice<ResponseNewsFeedListCommentCountDTO> page = newsfeedService.getList(pageable);
        return ResponseEntity.ok(page);

    }

    // 친구들의 뉴스피드 전체조회  	로그인한 사용자의 친구들이 쓴 글 목록
    @GetMapping("/friends")
    public ResponseEntity<Slice<ResponseNewsFeedListDTO>> getFriendsFeeds(
            @RequestParam(value = "page", defaultValue = "1") int clientPage
            ,HttpServletRequest request) {
        int row = 10; //피드 10개 , 한 페이지에나오는 행

        //로그인 유저
        Long loginUser =(Long) request.getSession(false).getAttribute("userId");

        // 클라이언트는 1-based 페이지 번호를 사용한다고 가정하고 0-based로 변환
        Pageable pageable = PageRequest.of(clientPage - 1, row, Sort.by("updatedAt").descending());
        Slice<ResponseNewsFeedListDTO> feedSlice = newsfeedService.getFriendNewsfeeds(loginUser, pageable);
        return ResponseEntity.ok(feedSlice);
    }

    // 친구한명 뉴스피드 전체조회   	(친구 한 명의 글 목록)
    @GetMapping("/users/{userId}")
    public ResponseEntity<Slice<ResponseNewsFeedListDTO>> getFriendNewsfeeds(
            @PathVariable("userId") Long userId, //프로필 클릭시 유저 받아오는걸로
            Pageable pageable) {
        Slice<ResponseNewsFeedListDTO> friendFeeds = newsfeedService.getFriendNewsfeeds(userId, pageable);
        return ResponseEntity.ok(friendFeeds);
    }
}
