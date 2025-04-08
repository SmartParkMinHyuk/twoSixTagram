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
        session.setAttribute("user", user); // 세션에 저장

        return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
    }

}

