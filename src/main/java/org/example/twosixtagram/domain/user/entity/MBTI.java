package org.example.twosixtagram.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum MBTI {

    ISTJ,
    ISFJ,
    INFJ,
    INTJ,
    ISTP,
    ISFP,
    INFP,
    INTP,
    ESTP,
    ESFP,
    ENFP,
    ENTP,
    ESTJ,
    ESFJ,
    ENFJ,
    ENTJ;

    @JsonCreator
    public static MBTI from(String value) {
        if (value == null || value.isBlank()) return null;

        for (MBTI mbti : MBTI.values()) {
            if (mbti.name().equalsIgnoreCase(value)) {
                return mbti;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 MBTI 값입니다: " + value);
    }
}