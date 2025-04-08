package org.example.twosixtagram.domain.user.entity;

public enum UserStatus {

    /**
     * req,res private MBTI mbti;
     * ACTIVE: 계정 활성 상태 (정상 사용 중)
     * UNACTIVE: 계정 비활성 상태 (삭제 혹은 비활성 처리됨)
     */
    ACTIVE("계정 활성화"),
    UNACTIVE("계정 비활성화");

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
