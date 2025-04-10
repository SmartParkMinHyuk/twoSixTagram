package org.example.twosixtagram.domain.friend.service;

import lombok.RequiredArgsConstructor;
import org.example.twosixtagram.domain.friend.dto.response.*;
import org.example.twosixtagram.domain.friend.entity.Friend;
import org.example.twosixtagram.domain.friend.entity.FriendStatus;
import org.example.twosixtagram.domain.friend.repository.FriendRepository;
import org.example.twosixtagram.domain.user.entity.User;
import org.example.twosixtagram.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

        if(friendRepository.findByFriend_IdAndUser_Id(userOpt.getId(),friendOpt.getId()).isPresent()||
                friendRepository.findByFriend_IdAndUser_Id(friendOpt.getId(),userOpt.getId()).isPresent())
        {
            throw new RuntimeException("잘못된 요청입니다.");
        }

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
        // id가 user인 경우
        List<Friend> byUserId = friendRepository.findByUser_IdAndStatus(id,FriendStatus.ACCEPTED);
        // id가 friend인 경우
        List<Friend> byFriendId = friendRepository.findByFriend_IdAndStatus(id, FriendStatus.ACCEPTED);

        // 둘 다 합치기
        List<GetFriendListResponseDto> friendList = new ArrayList<>();

        // user가 요청한 경우
        friendList.addAll(byUserId.stream()
                .map(friend -> new GetFriendListResponseDto(
                        friend.getFriend().getId(),
                        friend.getFriend().getEmail(),
                        friend.getFriend().getName()))
                .toList());

        // friend가 요청한 경우
        friendList.addAll(byFriendId.stream()
                .map(friend -> new GetFriendListResponseDto(
                        friend.getUser().getId(),
                        friend.getUser().getEmail(),
                        friend.getUser().getName()))
                .toList());

        return friendList;
    }


    @Override
    public void deleteFriend(Long id) {

        Friend byUserIdOrFriendId = friendRepository.findByUser_IdOrFriend_Id(id, id).orElseThrow(
                () -> new IllegalArgumentException("옳바르지 않은 요청입니다.")
        );

        friendRepository.delete(byUserIdOrFriendId);
    }


    @Override
    public GetProfileResponseDto getProfile(Long id) {

        // id(객체)가 user_id인지 friend_id인지 모르기 때문에 둘다 조회
        Friend profile = friendRepository.findByUser_IdOrFriend_Id(id,id).orElseThrow(
                () -> new IllegalArgumentException("옳바르지 않은 요청입니다."));

        // 만약 id(객체)가 friend_id일시
        if(profile.getFriend().getId().equals(id)){
            return new GetProfileResponseDto(
                    profile.getFriend().getId(),
                    profile.getFriend().getName(),
                    profile.getFriend().getEmail(),
                    profile.getFriend().getMbti());

        } else {
            // 만약 id(객체)가 user_id일시
            return new GetProfileResponseDto(
                    profile.getUser().getId(),
                    profile.getUser().getName(),
                    profile.getUser().getEmail(),
                    profile.getUser().getMbti());
        }

    }


}
