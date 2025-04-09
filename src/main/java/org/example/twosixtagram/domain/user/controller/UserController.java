package org.example.twosixtagram.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.twosixtagram.domain.user.dto.*;
import org.example.twosixtagram.domain.user.dto.UserSignupRequest;
import org.example.twosixtagram.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody UserSignupRequest request) {
        return new ResponseEntity<>(userService.signup(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginRequest request,
                                        HttpServletRequest httpRequest) {
        UserResponse user = userService.login(request);

        HttpSession session = httpRequest.getSession(true);
        session.setAttribute("userId", user.getId());

        return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UserUpdateRequest request,
                                                   HttpServletRequest httpRequest){
        HttpSession session = httpRequest.getSession(false);

        if (session == null || session.getAttribute("userId") == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        long userId = (long) session.getAttribute("userId");
        return new ResponseEntity<>(userService.updateUser(userId, request), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUser(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        long userId = (long) session.getAttribute("userId");
        return new ResponseEntity<>(userService.getUser(userId), HttpStatus.OK);
    }

}

