package org.example.twosixtagram.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.twosixtagram.domain.common.config.PasswordEncoder;
import org.example.twosixtagram.domain.user.dto.UserLoginRequest;
import org.example.twosixtagram.domain.user.dto.UserSignupRequest;
import org.example.twosixtagram.domain.user.dto.UserResponse;
import org.example.twosixtagram.domain.user.dto.UserUpdateRequest;
import org.example.twosixtagram.domain.user.entity.MBTI;
import org.example.twosixtagram.domain.user.entity.User;
import org.example.twosixtagram.domain.user.entity.UserStatus;
import org.example.twosixtagram.domain.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse signup(UserSignupRequest request) {

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .name(request.getName())
                .mbti(request.getMbti())
                .idNum(request.getIdNum())
                .status(UserStatus.ACTIVE)
                .build();

        return new UserResponse(userRepository.save(user));
    }

    public UserResponse login(UserLoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return new UserResponse(user);
    }

    @Transactional
    public UserResponse updateUser(long userId,UserUpdateRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        if (request.getEmail() != null) {
            user.changeEmail(request.getEmail());
        }

        if (request.getPassword() != null) {
            String encodedPassword = passwordEncoder.encode(request.getPassword());
            user.changePassword(encodedPassword);
        }

        if (request.getName() != null) {
            user.changeName(request.getName());
        }

        if (request.getMbti() != null) {
            user.changeMbti(request.getMbti()); // Enum 변환
        }

        return new UserResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        return new UserResponse(user);
    }

    @Transactional
    public void deleteUser(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        user.userRemove(UserStatus.UNACTIVE);
    }

}

