package org.example.twosixtagram.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.twosixtagram.domain.user.dto.UserLoginRequest;
import org.example.twosixtagram.domain.user.dto.UserSignupRequest;
import org.example.twosixtagram.domain.user.dto.UserResponse;
import org.example.twosixtagram.domain.user.entity.User;
import org.example.twosixtagram.domain.user.entity.UserStatus;
import org.example.twosixtagram.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse signup(UserSignupRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .mbti(request.getMbti())
                .idNum(request.getIdNum())
                .status(UserStatus.ACTIVE)
                .build();

        return new UserResponse(userRepository.save(user));
    }

    public UserResponse login(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return new UserResponse(user); // 세션 저장용 응답 객체
    }
}

