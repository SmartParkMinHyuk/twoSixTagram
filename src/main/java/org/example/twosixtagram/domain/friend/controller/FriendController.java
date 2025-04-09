package org.example.twosixtagram.domain.friend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.twosixtagram.domain.friend.dto.request.RequestFriendDto;
import org.example.twosixtagram.domain.friend.dto.request.UpdateRequestFriendDto;
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
}
