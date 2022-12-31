package com.example.projectboard.domain.type;

import lombok.Getter;

public enum SearchType {
    TITLE("제목"),
    CONTENT("본문"),
    HASHTAG("해시태그"),
    NICKNAME("작성자");

    @Getter
    private final String description;

    SearchType(String description) {
        this.description = description;
    }
}
