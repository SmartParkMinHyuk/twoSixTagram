package org.example.twosixtagram.domain.newsfeed.service;


import lombok.RequiredArgsConstructor;
import org.example.twosixtagram.domain.newsfeed.dto.*;
import org.example.twosixtagram.domain.newsfeed.entity.NewsFeed;
import org.example.twosixtagram.domain.newsfeed.repository.NewsfeedRepository;
import org.example.twosixtagram.domain.user.entity.User;
import org.example.twosixtagram.domain.user.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class NewsfeedServiceImpl implements NewsfeedService {


    //레포지토리 주입
    private final NewsfeedRepository newsfeedRepository;
    private final UserRepository userRepository;
//  private final CommentRepository commentRepository;


    //피드 상세보기 & 댓글리스트 포함
    @Override
    public ResponseNewsFeedDetailDTO getFeed(Long feedId, Pageable pageable) {

        //게시글 조회
        NewsFeed foundNewsFeed = newsfeedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다"));
        //new NotFoundException("NewsFeed not found")  예외 핸들러 쏴주기  //

        //게시글에 달린 댓글들 조회 findByNewsFeedId로  댓글에서 조회 해서 가져오기

        //toDTO 변환해주고

        //detailDTO에 넣어서 반환
        return ResponseNewsFeedDetailDTO.builder()
                .feedId(foundNewsFeed.getId())
                .userName(foundNewsFeed.getUser().getName())
                .userEmail(foundNewsFeed.getUser().getEmail())
                .title(foundNewsFeed.getTitle())
                .contents(foundNewsFeed.getContents())
                .updatedAt(foundNewsFeed.getUpdatedAt())
//                .commentList() 댓글넣기
                .build();

    }

    //피드 등록
    @Transactional
    @Override
    public ResponseNewsfeedDTO register(RequestNewsfeedDTO requestNewsfeedDTO, Long userId) {
        //유저 조회
//      User user = UserRepository.findByIdOrElseThrow(loginUser); 디폴트로 ?
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        //toEntity
        NewsFeed foundNewsFeed = requestNewsfeedDTO.toEntity(user);
        if(!foundNewsFeed.getUser().getId().equals(user.getId())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "권한이 없습니다.");
            // new UnauthorizedException("Unauthorized"); 예외 핸들러 쏴주기
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

        return ResponseUpdateNewsFeedDTO.builder()
                .id(foundNewsFeed.getId())
                .userName(foundNewsFeed.getUser().getName())
                .userEmail(foundNewsFeed.getUser().getEmail())
                .title(foundNewsFeed.getTitle())
                .contents(foundNewsFeed.getContents())
                .updatedAt(foundNewsFeed.getUpdatedAt())
                .build();
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
            throw new IllegalArgumentException("권한이 없습니다.");
            // new UnauthorizedException("Unauthorized"); 예외 핸들러 쏴주기
        }

        //게시글 삭제시 해당 게시글 댓글 전체 삭제   나중에 JPQL 찾아보고 ㄱㄱ 댓글 완료되면 진행 **
//      commentsRepository.deleteAllByNewsFeedById(id);//newsFeedId
        newsfeedRepository.delete(newsFeed);
    }
}
