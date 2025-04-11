package org.example.twosixtagram.domain.friend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.twosixtagram.domain.friend.dto.request.AcceptStatusRequestDto;
import org.example.twosixtagram.domain.friend.dto.request.RequestFriendDto;
import org.example.twosixtagram.domain.friend.dto.response.*;
import org.example.twosixtagram.domain.friend.service.FriendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/friends")
@RestController
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    // 친구 요청 API
    // Pathvariable에 요청자 id(주체) RequestBody에 요청받는 사람 id(객체)
    @PostMapping
    public ResponseEntity<SaveStatusResponseDto> saveStatus(
            HttpServletRequest httpServletRequest,
            @RequestBody @Valid RequestFriendDto dto){

        Long userId = (Long) httpServletRequest.getSession(false).getAttribute("userId");

        SaveStatusResponseDto saveStatusResponseDto = friendService.saveStatus(userId, dto.getFriendId(), dto.getStatus());

        return new ResponseEntity<>(saveStatusResponseDto,HttpStatus.CREATED);
    }

    // 친구 요청 확인 API
    // PathVariable에 요청받은 사람 id(주체)
    @GetMapping("/check")
    public ResponseEntity<List<GetStatusResponseDto>> getStatus(
            HttpServletRequest httpServletRequest
    ){
        Long friendId = (Long) httpServletRequest.getSession(false).getAttribute("userId");

        List<GetStatusResponseDto> friend = friendService.getStatus(friendId);

        return new ResponseEntity<>(friend,HttpStatus.OK);
    }

    // 친구 수락 API
    // PathVariable에 요청 받은 사람 id(주체) RequestBody에 요청자의 id(객체)
    @PatchMapping
    public ResponseEntity<AcceptStatusResponseDto> acceptStatus(
            HttpServletRequest httpServletRequest,
            @RequestBody AcceptStatusRequestDto dto
            ){

        Long friendId = (Long) httpServletRequest.getSession(false).getAttribute("userId");

        // AcceptStatusRequestDto에 들어올 수 있는 건 "ACCEPTED" 혹은 "DECLIEND"
        AcceptStatusResponseDto acceptStatusResponseDto = friendService.acceptStatus(friendId,dto.getUserId(),dto.getStatus());

        return new ResponseEntity<>(acceptStatusResponseDto,HttpStatus.OK);

    }

    // 친구 전체 목록 조회 API
    // PathVariable에 id(주체)
    @GetMapping
    public ResponseEntity<List<GetFriendListResponseDto>> getFriendList(HttpServletRequest httpServletRequest){

        Long id = (Long) httpServletRequest.getSession(false).getAttribute("userId");

        List<GetFriendListResponseDto> friendList = friendService.getFriendList(id);

        return new ResponseEntity<>(friendList,HttpStatus.OK);
    }

    // 친구 삭제
    // PathVariable에 id(객체)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFriend(@PathVariable Long id){

        friendService.deleteFriend(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 친구 프로필 조회
    // PathVariable에 id(객체)
    @GetMapping("/profile/{id}")
    public ResponseEntity<GetProfileResponseDto> getProfile(@PathVariable Long id){

        GetProfileResponseDto profile = friendService.getProfile(id);

        return new ResponseEntity<>(profile,HttpStatus.OK);

    }


}
