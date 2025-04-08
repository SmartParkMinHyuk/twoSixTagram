package org.example.twosixtagram.domain.newsfeed.repository;

import org.example.twosixtagram.domain.newsfeed.entity.NewsFeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsfeedRepository extends JpaRepository<NewsFeed,Long> {
}
