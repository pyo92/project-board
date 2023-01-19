package com.example.projectboard.dto.response;

import com.example.projectboard.dto.ArticleDto;
import com.example.projectboard.dto.HashtagDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleResponse(
        Long id,
        String title,
        String content,
        Set<String> hashtags,
        LocalDateTime createdAt,
        String email,
        String nickname
) implements Serializable {

    public static ArticleResponse of(Long id, String title, String content, Set<String> hashtags, LocalDateTime createdAt, String email, String nickname) {
        return new ArticleResponse(id, title, content, hashtags, createdAt, email, nickname);
    }

    public static ArticleResponse from(ArticleDto dto) {
        String nickname = dto.memberDto().nickName();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.memberDto().userId();
        }

        return new ArticleResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.hashTagDtos().stream()
                        .map(HashtagDto::name)
                        .collect(Collectors.toUnmodifiableSet()),
                dto.createdAt(),
                dto.memberDto().email(),
                nickname
        );
    }
}
