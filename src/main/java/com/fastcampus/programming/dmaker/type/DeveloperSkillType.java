package com.fastcampus.programming.dmaker.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeveloperSkillType {
    BACK_END("백엔드 개발자"),
    FRONT_END("프로트엔드 개발자"),
    FULL_STACK("풀스텍 개발자");

    private final String desctiption;
}
