package org.example.twosixtagram.domain.friend.service;

import lombok.RequiredArgsConstructor;
import org.example.twosixtagram.domain.friend.dto.response.AcceptStatusResponseDto;
import org.example.twosixtagram.domain.friend.dto.response.GetFriendListResponseDto;
import org.example.twosixtagram.domain.friend.dto.response.GetStatusResponseDto;
import org.example.twosixtagram.domain.friend.dto.response.SaveStatusResponseDto;
import org.example.twosixtagram.domain.friend.entity.Friend;
import org.example.twosixtagram.domain.friend.entity.FriendStatus;
import org.example.twosixtagram.domain.friend.repository.FriendRepository;
import org.example.twosixtagram.domain.user.entity.User;
import org.example.twosixtagram.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;

    private final UserRepository userRepository;

    @Override
    public SaveStatusResponseDto saveStatus(Long id, Long friendId, FriendStatus status) {
        // userId, friendId가 있는지 확인과 동시에 인스턴스화
        User userOpt = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("유저가 없습니다: "+ id));
        User friendOpt = userRepository.findById(friendId).orElseThrow(
                () -> new IllegalArgumentException("유저가 없습니다: "+ friendId));

        // userId,friendId,status를 Friend 엔티티 타입으로 인스턴스화
        Friend saveStatus = new Friend(userOpt, friendOpt, status);

        friendRepository.save(saveStatus);
        // 프론트에서 friend의 정보를 확인하고
        return new SaveStatusResponseDto(friendOpt.getId(),friendOpt.getEmail(),friendOpt.getName(),status);
    }

    @Override
    public List<GetStatusResponseDto> getStatus(Long friendId) {

        // 입럭받은 friendId로 friend_id컬럼 조회 및 status컬럼에 PENDING인 로우를 전체 조회
        List<Friend> pendingFriend = friendRepository.findByFriend_IdAndStatus(friendId,FriendStatus.PENDING);

        // pendingFriend가 비었을 시 요청 없음 반환
        if(pendingFriend.isEmpty()){
            throw new IllegalArgumentException("친구 요청이 없습니다.");
        }

        // pendingFriend을 stream으로 GetStatusResponseDto에 매핑
        List<GetStatusResponseDto> dtolist = pendingFriend.stream()
                .map(friend -> new GetStatusResponseDto(
                        friend.getUser().getId(),
                        friend.getUser().getEmail(),
                        friend.getUser().getName()))
                .toList();

        return dtolist ;

    }

    @Transactional
    @Override
    public AcceptStatusResponseDto acceptStatus(Long friendId, Long userId ,FriendStatus status) {
        // status에 ACCEPTED가 들어오면 update 메서드를 통해 상태 변경
        if(status == FriendStatus.ACCEPTED) {
            Friend byFriendId1 = friendRepository.findByFriend_IdAndUser_Id(friendId,userId).orElseThrow(
                    () -> new IllegalArgumentException("요청이 없습니다."));

            byFriendId1.updateStatus(status);

            return new AcceptStatusResponseDto(byFriendId1.getStatus());
            // status에 DECLINED가 들어오면 테이블 삭제 및 예외처리로 메세지 출력
        } else {
            Friend byFriendId2 = friendRepository.findByFriend_IdAndUser_Id(friendId,userId).orElseThrow(
                    () -> new IllegalArgumentException("요청이 없습니다."));

            friendRepository.delete(byFriendId2);

            throw new IllegalArgumentException("친구요청이 거절 되었습니다.");
        }
    }

    @Override
    public List<GetFriendListResponseDto> getFriendList(Long id) {

        // userId와 status "ACCEPTED"에 알맞는 모든 테이블 로우 찾기
        List<Friend> byUserId = friendRepository.findByUser_IdAndStatus(id,FriendStatus.ACCEPTED);

        // 찾은 테이블들을 모두 GetFriendListResponseDto 형식에 알맞게 스트림 매핑
        List<GetFriendListResponseDto> friendList = byUserId.stream()
                .map(friend -> new GetFriendListResponseDto(
                        friend.getFriend().getEmail(),friend.getFriend().getName()))
                .toList();

        return friendList;
    }


}
