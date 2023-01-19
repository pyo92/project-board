package com.example.projectboard.dto;

import com.example.projectboard.domain.Article;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleWithCommentsDto(
        Long id,
        MemberDto memberDto,
        List<ArticleCommentDto> articleCommentDtos,
        String title,
        String content,
        Set<HashtagDto> hashTagDtos,
        String createdBy,
        LocalDateTime createdAt,
        String modifiedBy,
        LocalDateTime modifiedAt
) {
    public static ArticleWithCommentsDto of(Long id, MemberDto memberDto, List<ArticleCommentDto> articleCommentDtos, String title, String content, Set<HashtagDto> hashTagDtos, String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt) {
        return new ArticleWithCommentsDto(id, memberDto, articleCommentDtos, title, content, hashTagDtos, createdBy, createdAt, modifiedBy, modifiedAt);
    }

    public static ArticleWithCommentsDto from(Article entity) {
        return new ArticleWithCommentsDto(
                entity.getId(),
                MemberDto.from(entity.getMember()),
                entity.getArticleComments().stream()
                        .map(ArticleCommentDto::from)
                        .collect(Collectors.toCollection(ArrayList::new)),
                entity.getTitle(),
                entity.getContent(),
                entity.getHashtags().stream()
                        .map(HashtagDto::from)
                        .collect(Collectors.toUnmodifiableSet()),
                entity.getCreatedBy(),
                entity.getCreatedAt(),
                entity.getModifiedBy(),
                entity.getModifiedAt()
        );
    }
}
