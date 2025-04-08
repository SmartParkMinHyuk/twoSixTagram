package org.example.twosixtagram.domain.comment.repository;

import org.example.twosixtagram.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByNewsFeedId(Long newsFeedId);
}
