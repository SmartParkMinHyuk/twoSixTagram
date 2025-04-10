package org.example.twosixtagram.domain.newsfeed.repository;

import org.example.twosixtagram.domain.newsfeed.dto.response.ResponseNewsFeedListCommentCountDTO;
import org.example.twosixtagram.domain.newsfeed.dto.response.ResponseNewsFeedListDTO;
import org.example.twosixtagram.domain.newsfeed.entity.NewsFeed;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsfeedRepository extends JpaRepository<NewsFeed,Long> {
    //친구들의 피드들을 전체조회  ,
    Slice<NewsFeed> findByUserIdIn(List<Long> userIds, Pageable pageable);

    //게시글 하나당 댓글 count 조회
    @Query("SELECT new org.example.twosixtagram.domain.newsfeed.dto.response.ResponseNewsFeedListCommentCountDTO(" +
            "n.id, n.title, n.contents,n.user.name,n.user.email,n.createdAt, n.updatedAt, COUNT(c)" +
            ") " +
            "FROM NewsFeed n LEFT JOIN n.comments c " +
            "GROUP BY n.id, n.title, n.contents, n.createdAt " +
            "ORDER BY n.createdAt DESC")
    Slice<ResponseNewsFeedListCommentCountDTO> findAllWithCommentCount(Pageable pageable);

    //유저로 전체조회
    Slice<NewsFeed> findByUserIdOrderByUpdatedAtDesc(Long userId, Pageable pageable);
}
