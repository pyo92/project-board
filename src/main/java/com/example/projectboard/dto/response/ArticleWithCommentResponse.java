package com.example.projectboard.dto.response;

import com.example.projectboard.dto.ArticleCommentDto;
import com.example.projectboard.dto.ArticleWithCommentsDto;
import com.example.projectboard.dto.HashtagDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
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
        Set<ArticleCommentResponse> articleCommentsResponse
) implements Serializable {

    public static ArticleWithCommentResponse of(Long id, String title, String content, Set<String> hashtags, LocalDateTime createdAt, String userId, String email, String nickname, Set<ArticleCommentResponse> articleCommentResponses) {
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
                organizeChildComments(dto.articleCommentDtos())
        );
    }

    private static Set<ArticleCommentResponse> organizeChildComments(Set<ArticleCommentDto> dtos) {
        Map<Long, ArticleCommentResponse> map = dtos.stream()
                .map(ArticleCommentResponse::from)
                .collect(Collectors.toMap(ArticleCommentResponse::id, Function.identity()));

        map.values().stream()
                .filter(ArticleCommentResponse::hasParentComment)
                .forEach(child -> {
                    ArticleCommentResponse parent = map.get(child.parentCommentId());
                    parent.childComments().add(child);
                });

        return map.values().stream()
                .filter(comment -> !comment.hasParentComment())
                .collect(Collectors.toCollection(() ->
                    new TreeSet<>(Comparator
                            .comparing(ArticleCommentResponse::createdAt)
                            .reversed()
                            .thenComparingLong(ArticleCommentResponse::id)
                    )
                ));
    }
}
