package com.example.projectboard.dto;

import com.example.projectboard.domain.Article;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public record ArticleWithCommentsDto(
        Long id,
        MemberDto memberDto,
        List<ArticleCommentDto> articleCommentDtos,
        String title,
        String content,
        String hashTag,
        String createdBy,
        LocalDateTime createdAt,
        String modifiedBy,
        LocalDateTime modifiedAt
) {
    public static ArticleWithCommentsDto of(Long id, MemberDto memberDto, List<ArticleCommentDto> articleCommentDtos, String title, String content, String hashTag, String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt) {
        return new ArticleWithCommentsDto(id, memberDto, articleCommentDtos, title, content, hashTag, createdBy, createdAt, modifiedBy, modifiedAt);
    }

    public static ArticleWithCommentsDto from(Article entity) {
        return new ArticleWithCommentsDto(
                entity.getId(),
                MemberDto.from(entity.getMember()),
                entity.getArticleComments().stream().map(ArticleCommentDto::from).collect(Collectors.toCollection(LinkedList::new)),
                entity.getTitle(),
                entity.getContent(),
                entity.getHashTag(),
                entity.getCreatedBy(),
                entity.getCreatedAt(),
                entity.getModifiedBy(),
                entity.getModifiedAt()
        );
    }
}
