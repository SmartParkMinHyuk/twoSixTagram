package org.example.twosixtagram.domain.newsfeed.service;

import org.example.twosixtagram.domain.newsfeed.dto.*;
import org.springframework.data.domain.Pageable;

public interface NewsfeedService {
    //피드 단건 조회
    ResponseNewsFeedDetailDTO getFeed(Long id, Pageable pageable);

    //피드등록
    ResponseNewsfeedDTO register(RequestNewsfeedDTO requestNewsfeedDTO, Long userId);

    //피드 수정
    ResponseUpdateNewsFeedDTO updateFeed(Long id, RequestUpdateNewsFeedDTO requestUpdateNewsFeedDTO, Long user);

    //삭제
    void deleteNewsFeed(Long id, Long loginId);
}
