package org.example.twosixtagram.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.example.twosixtagram.domain.user.entity.MBTI;

@Getter
public class UserUpdateRequest {

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 8자 이상이며, 대문자, 소문자, 숫자, 특수문자를 모두 포함해야 합니다."
    )
    private String password;

    private String name;

    private MBTI mbti;

    private String idNum;
}
