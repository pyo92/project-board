package com.example.projectboard.dto.response;

import com.example.projectboard.dto.ArticleWithCommentsDto;
import com.example.projectboard.dto.HashtagDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleWithCommentResponse(
        Long id,
        String title,
        String content,
        Set<String> hashtags,
        LocalDateTime createdAt,
        String userId,
        String email,
        String nickname,
        List<ArticleCommentResponse> articleCommentsResponse
) implements Serializable {

    public static ArticleWithCommentResponse of(Long id, String title, String content, Set<String> hashtags, LocalDateTime createdAt, String userId, String email, String nickname, List<ArticleCommentResponse> articleCommentResponses) {
        return new ArticleWithCommentResponse(id, title, content, hashtags, createdAt, userId, email, nickname, articleCommentResponses);
    }

    public static ArticleWithCommentResponse from(ArticleWithCommentsDto dto) {
        String nickname = dto.memberDto().nickName();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.memberDto().userId();
        }

        return new ArticleWithCommentResponse(
                dto.id(),
                dto.title(),
                dto.content(),
                dto.hashTagDtos().stream()
                        .map(HashtagDto::name)
                        .collect(Collectors.toUnmodifiableSet()),
                dto.createdAt(),
                dto.memberDto().userId(),
                dto.memberDto().email(),
                nickname,
                dto.articleCommentDtos().stream()
                        .map(ArticleCommentResponse::from)
                        .collect(Collectors.toCollection(ArrayList::new))
        );
    }
}
