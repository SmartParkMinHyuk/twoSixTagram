package org.example.twosixtagram.domain.friend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.twosixtagram.domain.friend.dto.request.AcceptStatusRequestDto;
import org.example.twosixtagram.domain.friend.dto.request.RequestFriendDto;
import org.example.twosixtagram.domain.friend.dto.request.UpdateRequestFriendDto;
import org.example.twosixtagram.domain.friend.dto.response.AcceptStatusResponseDto;
import org.example.twosixtagram.domain.friend.dto.response.GetFriendListResponseDto;
import org.example.twosixtagram.domain.friend.dto.response.GetStatusResponseDto;
import org.example.twosixtagram.domain.friend.dto.response.SaveStatusResponseDto;
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
    @PostMapping("/{userId}")
    public ResponseEntity<SaveStatusResponseDto> saveStatus(
            @PathVariable Long userId,
            @RequestBody @Valid RequestFriendDto dto){

        SaveStatusResponseDto saveStatusResponseDto = friendService.saveStatus(userId, dto.getFriendId(), dto.getStatus());

        return new ResponseEntity<>(saveStatusResponseDto,HttpStatus.CREATED);
    }

    // 친구 요청 확인 API
    @GetMapping("/{friendId}")
    public ResponseEntity<List<GetStatusResponseDto>> getStatus(@PathVariable Long friendId){

        List<GetStatusResponseDto> friend = friendService.getStatus(friendId);

        return new ResponseEntity<>(friend,HttpStatus.OK);
    }

    // 친구 수락 API
    @PatchMapping("/{friendId}")
    public ResponseEntity<AcceptStatusResponseDto> acceptStatus(
            @PathVariable Long friendId,
            @RequestBody AcceptStatusRequestDto dto
            ){

        // AcceptStatusRequestDto에 들어올 수 있는 건 "ACCEPTED" 혹은 "DECLIEND"
        AcceptStatusResponseDto acceptStatusResponseDto = friendService.acceptStatus(friendId,dto.getUserId(),dto.getStatus());

        return new ResponseEntity<>(acceptStatusResponseDto,HttpStatus.OK);

    }

    // 친구 전체 목록 조회 API
    @GetMapping("/{id}")
    public ResponseEntity<List<GetFriendListResponseDto>> getFriendList(@PathVariable Long id){

        List<GetFriendListResponseDto> friendList = friendService.getFriendList(id);

        return new ResponseEntity<>(friendList,HttpStatus.OK);
    }


}
