package org.example.twosixtagram.domain.newsfeed.service;

import org.example.twosixtagram.domain.friend.dto.response.GetFriendListResponseDto;
import org.example.twosixtagram.domain.newsfeed.dto.request.RequestNewsfeedDTO;
import org.example.twosixtagram.domain.newsfeed.dto.request.RequestUpdateNewsFeedDTO;
import org.example.twosixtagram.domain.newsfeed.dto.response.*;
import org.example.twosixtagram.domain.newsfeed.entity.NewsFeed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface NewsfeedService {
    // 비로그인시 제목만 조회
    Slice<ResponseUnauthenticatedDTO> getTitleFeeds(Pageable pageable);

    // 친구피드 전체조회
    Slice<ResponseNewsFeedListDTO> getFriendNewsfeeds(Long userId,Pageable pageable);

    // 친구들피드만 전체 조회
    public Slice<ResponseNewsFeedListDTO> findByUserIdIn(Long userId, Pageable pageable);

    // 피드 전체 조회
//    Slice<ResponseNewsFeedListDTO> getList(Pageable pageable);

    //댓글수 포함 전체조회
    Slice<ResponseNewsFeedListCommentCountDTO> getList(Pageable pageable);

    //피드 단건 조회
    ResponseNewsFeedDetailDTO getFeed(Long id, Pageable pageable);

    //피드등록
    ResponseNewsfeedDTO register(RequestNewsfeedDTO requestNewsfeedDTO, Long userId);

    //피드 수정
    ResponseUpdateNewsFeedDTO updateFeed(Long id, RequestUpdateNewsFeedDTO requestUpdateNewsFeedDTO, Long user);

    //삭제
    void deleteNewsFeed(Long id, Long loginId);
}
