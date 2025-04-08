package org.example.twosixtagram.domain.newsfeed.service;

import jakarta.validation.Valid;
import org.example.twosixtagram.domain.newsfeed.dto.RequestNewsfeedDTO;
import org.example.twosixtagram.domain.newsfeed.dto.ResponseNewsfeedDTO;
import org.example.twosixtagram.domain.user.entity.User;

public interface NewsfeedService {

    //피드등록
    ResponseNewsfeedDTO register(RequestNewsfeedDTO requestNewsfeedDTO, User user);
    //삭제
    void deleteNewsFeed(Long id, Long loginId);
}
