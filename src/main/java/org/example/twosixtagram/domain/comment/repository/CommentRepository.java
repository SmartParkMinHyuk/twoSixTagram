package org.example.twosixtagram.domain.comment.repository;

import org.example.twosixtagram.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Comment c WHERE c.newsFeed.id = :newsFeedId")
    void deleteAllByNewsFeedId(@Param("newsFeedId") Long newsFeedId);
    Page<Comment> findByNewsFeedId(Long feedId, Pageable pageable);
}
