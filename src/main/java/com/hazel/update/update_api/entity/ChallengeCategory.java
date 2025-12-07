package com.hazel.update.update_api.entity;

public enum ChallengeCategory {
    STUDY("공부"),
    HEALTH("운동"),
    READING("독서"),
    LIFE("생활습관"),
    ETC("기타");

    private final String koreanName;

    ChallengeCategory(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return koreanName;
    }
}
