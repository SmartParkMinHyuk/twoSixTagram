package org.example.twosixtagram.domain.friend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.twosixtagram.domain.friend.dto.request.RequestFriendDTO;
import org.example.twosixtagram.domain.friend.dto.response.SaveStatusResponseDto;
import org.example.twosixtagram.domain.friend.service.FriendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/friends")
@RestController
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/{userId}")
    public ResponseEntity<SaveStatusResponseDto> saveStatus(
            @PathVariable Long userId,
            @RequestBody @Valid RequestFriendDTO dto){

        SaveStatusResponseDto saveStatusResponseDto = friendService.saveStatus(userId, dto.getFriendId(), dto.getStatus());

        return new ResponseEntity<>(saveStatusResponseDto,HttpStatus.OK);
    }

}
