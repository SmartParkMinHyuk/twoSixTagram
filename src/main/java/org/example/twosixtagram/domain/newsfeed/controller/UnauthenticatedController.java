package org.example.twosixtagram.domain.newsfeed.controller;


import lombok.RequiredArgsConstructor;
import org.example.twosixtagram.domain.newsfeed.dto.response.ResponseUnauthenticatedDTO;
import org.example.twosixtagram.domain.newsfeed.service.NewsfeedService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/unauthenticated")
public class UnauthenticatedController {

    private final NewsfeedService newsfeedService;

    //WHITE_LIST에 url추가하기
    //피드 제목만 리스트로(페이지 포함) 받기,
    @GetMapping
    public ResponseEntity<Slice<ResponseUnauthenticatedDTO>> getFeedTitleList(
            @RequestParam(value = "page", defaultValue = "1") int clientPage
    ){
        int row = 10; // 피드 10개 , 한 페이지에나오는 행
        Pageable pageable = PageRequest.of(clientPage - 1, row, Sort.by("updatedAt").descending());
        Slice<ResponseUnauthenticatedDTO> titleSlice = newsfeedService.getTitleFeeds(pageable);
        return ResponseEntity.ok(titleSlice);
    }


}
