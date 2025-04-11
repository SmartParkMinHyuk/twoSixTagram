package org.example.twosixtagram.domain.newsfeed.service;


import lombok.RequiredArgsConstructor;
import org.example.twosixtagram.domain.comment.dto.ResponseCommentDTO;
import org.example.twosixtagram.domain.comment.entity.Comment;
import org.example.twosixtagram.domain.comment.repository.CommentRepository;
import org.example.twosixtagram.domain.friend.dto.response.GetFriendListResponseDto;
import org.example.twosixtagram.domain.friend.service.FriendService;
import org.example.twosixtagram.domain.newsfeed.dto.request.RequestNewsfeedDTO;
import org.example.twosixtagram.domain.newsfeed.dto.request.RequestUpdateNewsFeedDTO;
import org.example.twosixtagram.domain.newsfeed.dto.response.*;
import org.example.twosixtagram.domain.newsfeed.entity.NewsFeed;
import org.example.twosixtagram.domain.newsfeed.repository.NewsfeedRepository;
import org.example.twosixtagram.domain.user.entity.User;
import org.example.twosixtagram.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NewsfeedServiceImpl implements NewsfeedService {

    //레포지토리 주입
    private final NewsfeedRepository newsfeedRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final FriendService friendService;

    //제목만 조회 ,페이징처리(비로그인시 사용)
    @Transactional(readOnly = true)
    @Override
    public Slice<ResponseUnauthenticatedDTO> getTitleFeeds(Pageable pageable) {
        //다 조회해서
        Slice<NewsFeed> list = newsfeedRepository.findAll(pageable);
        //제목만 dto에 넣기
        List<ResponseUnauthenticatedDTO> titleList = list.getContent().stream().map(feed ->
                ResponseUnauthenticatedDTO.builder()
                        .title(feed.getTitle())
                        .build()).toList();

        return new SliceImpl<>(titleList,pageable,list.hasNext());
    }
    @Override
    @Transactional(readOnly = true)
    public Slice<ResponseNewsFeedListDTO> getFriendNewsfeeds(Long userId, Pageable pageable) {

        Slice<NewsFeed> feeds = newsfeedRepository.findByUserIdOrderByUpdatedAtDesc(userId, pageable);

        List<ResponseNewsFeedListDTO> respDTO = feeds.stream().map(ResponseNewsFeedListDTO::toDTO).toList();
        return new SliceImpl<>(respDTO,pageable,feeds.hasNext());
    }

    @Transactional(readOnly = true)
    @Override
    public Slice<ResponseNewsFeedListDTO> findByUserIdIn(Long userId, Pageable pageable) {
        //친구 목록가져오기
        List<GetFriendListResponseDto> friendList = friendService.getFriendList(userId);
        //친구 아이디
        List<Long> friendIds =friendList.stream().map(GetFriendListResponseDto::getId).toList();
        //친구들 id로
        Slice<NewsFeed> feedSlice = newsfeedRepository.findByUserIdIn(friendIds, pageable);

        List<ResponseNewsFeedListDTO> dtoList = feedSlice.getContent().stream()
                .map(ResponseNewsFeedListDTO::toDTO)
                .toList();

        return new SliceImpl<>(dtoList, pageable, feedSlice.hasNext());
    }

    //전체보기  댓글 count 포함
    @Transactional
    @Override
    public Slice<ResponseNewsFeedListCommentCountDTO> getList(Pageable pageable) {
        return newsfeedRepository.findAllWithCommentCount(pageable);
    }

    //피드 상세보기 & 댓글리스트 포함 (hasNext로 무한스크롤)
    @Transactional
    @Override
    public ResponseNewsFeedDetailDTO getFeed(Long id, Pageable pageable) {

        //게시글 조회
        NewsFeed foundNewsFeed = newsfeedRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다"));
        //new NotFoundException("NewsFeed not found")  예외 핸들러 쏴주기  //

        //게시글에 달린 댓글들 조회 findByNewsFeedId로  댓글에서 조회 해서 가져오기
        Page<Comment> comments = commentRepository.findByNewsFeedId(id, pageable);

        //toDTO 변환해주고
        List<ResponseCommentDTO> commentDTOs = comments.getContent().stream()
                .map(comment -> ResponseCommentDTO.builder()
                        .id(comment.getId())
                        .contents(comment.getContents())
                        .userId(comment.getUser().getId())
                        .userName(comment.getUser().getName())
                        .build())
                .toList();

        //detailDTO에 넣어서 반환
        return ResponseNewsFeedDetailDTO.toDTO(foundNewsFeed,commentDTOs,comments.hasNext());
    }

    //피드 등록
    @Transactional
    @Override
    public ResponseNewsfeedDTO register(RequestNewsfeedDTO requestNewsfeedDTO, Long userId) {
        //유저 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        //toEntity
        NewsFeed foundNewsFeed = requestNewsfeedDTO.toEntity(user);
        if(!foundNewsFeed.getUser().getId().equals(user.getId())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "권한이 없습니다.");
        }

        // 저장 (영속)
        NewsFeed saveFeed = newsfeedRepository.save(foundNewsFeed);

        //toDTO응답
        return ResponseNewsfeedDTO.toDTO(saveFeed);
    }

    @Transactional
    @Override
    public ResponseUpdateNewsFeedDTO updateFeed(Long feedId, RequestUpdateNewsFeedDTO requestUpdateNewsFeedDTO, Long userId) {
        //수정할 피드 조회
       NewsFeed foundNewsFeed = newsfeedRepository.findById(feedId)
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,("게시물이 존재하지 않습니다.")));
        //new NotFoundException("NewsFeed not found")  예외 핸들러 쏴주기

       //게시물 유저 로그인유저 검증
        if(!foundNewsFeed.getUser().getId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "권한이 없습니다.");
            // new UnauthorizedException("Unauthorized"); 예외 핸들러 쏴주기
        }

       //수정 ( 변경감지 )
        foundNewsFeed.update(requestUpdateNewsFeedDTO.getTitle(),requestUpdateNewsFeedDTO.getContents());

        return ResponseUpdateNewsFeedDTO.toDTO(foundNewsFeed);
    }

    //피드 삭제
    @Transactional
    @Override
    public void deleteNewsFeed(Long id, Long loginId) {
        //삭제할 피드 조회
        NewsFeed newsFeed = newsfeedRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,("게시물이 존재하지 않습니다.")));
        //new NotFoundException("NewsFeed not found")  예외 핸들러 쏴주기

        //피드의 id랑 세션이랑 맞는지 검증
        if(!newsFeed.getUser().getId().equals(loginId)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "권한이 없습니다.");
            // new UnauthorizedException("Unauthorized"); 예외 핸들러 쏴주기
        }

        //게시글 삭제시 해당 게시글 댓글 전체 삭제
        commentRepository.deleteAllByNewsFeedId(id);  //벌크연산
        newsfeedRepository.delete(newsFeed);
    }

//    전체보기
//    @Transactional
//    @Override
//    public Slice<ResponseNewsFeedListDTO> getList(Pageable pageable) {
//
//        //게시글 전체 조회 -> slice
//        Slice<NewsFeed> feedPage = newsfeedRepository.findAll(pageable);
//        //dto로 변환
//        // 엔티티를 DTO로 변환
//        List<ResponseNewsFeedListDTO> dtoList = feedPage.getContent().stream().map(ResponseNewsFeedListDTO::toDTO).toList();
//
//        return new SliceImpl<>(dtoList, pageable, feedPage.hasNext());
//    }
}
