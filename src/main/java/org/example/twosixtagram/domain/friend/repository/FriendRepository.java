package org.example.twosixtagram.domain.friend.repository;

import org.example.twosixtagram.domain.friend.entity.Friend;
import org.example.twosixtagram.domain.friend.entity.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend,Long> {

    Optional<Friend> findByUseridAndFriendId(Long userId, Long friendId);

    List<Friend> findByFriend_IdAndStatus(Long friendId, FriendStatus status);

    Optional<Friend> findByFriend_IdAndUser_Id(Long friendId,Long userId);
}
