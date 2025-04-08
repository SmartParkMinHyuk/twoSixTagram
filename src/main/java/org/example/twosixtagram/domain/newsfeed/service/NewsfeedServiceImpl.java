package org.example.twosixtagram.domain.newsfeed.service;


import lombok.RequiredArgsConstructor;
import org.example.twosixtagram.domain.comment.repository.CommentRepository;
import org.example.twosixtagram.domain.newsfeed.dto.RequestNewsfeedDTO;
import org.example.twosixtagram.domain.newsfeed.dto.ResponseNewsfeedDTO;
import org.example.twosixtagram.domain.newsfeed.entity.NewsFeed;
import org.example.twosixtagram.domain.newsfeed.repository.NewsfeedRepository;
import org.example.twosixtagram.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NewsfeedServiceImpl implements NewsfeedService {

    //레포지토리 주입
    private final NewsfeedRepository newsfeedRepository;
//    private final CommentRepository commentRepository;

    //피드 등록
    @Transactional
    @Override
    public ResponseNewsfeedDTO register(RequestNewsfeedDTO requestNewsfeedDTO, User user) {
        //toEntity
        NewsFeed feed = requestNewsfeedDTO.toEntity(user);

        // 저장 (영속)
        NewsFeed saveFeed = newsfeedRepository.save(feed);

        //toDTO응답
        return ResponseNewsfeedDTO.toDTO(saveFeed);
    }

    //피드 삭제
    @Transactional
    @Override
    public void deleteNewsFeed(Long id, Long loginId) {
        //삭제할 피드 조회
        NewsFeed newsFeed = newsfeedRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("피드가 존재하지 않습니다."));
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
