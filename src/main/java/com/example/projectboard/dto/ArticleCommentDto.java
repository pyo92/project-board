package com.example.projectboard.dto;

import com.example.projectboard.domain.Article;
import com.example.projectboard.domain.ArticleComment;
import com.example.projectboard.domain.Member;

import java.time.LocalDateTime;

public record ArticleCommentDto(
        Long id,
        Long articleId,
        MemberDto memberDto,
        Long parentCommentId,
        String content,
        String createdBy,
        LocalDateTime createdAt,
        String modifiedBy,
        LocalDateTime modifiedAt
) {

    public static ArticleCommentDto of(Long articleId, MemberDto memberDto, String content) {
        return ArticleCommentDto.of(articleId, memberDto, null, content);
    }

    public static ArticleCommentDto of(Long articleId, MemberDto memberDto, Long parentCommentId, String content) {
        return ArticleCommentDto.of(null, articleId, memberDto, parentCommentId, content, null, null, null, null);
    }

    public static ArticleCommentDto of(Long id, Long articleId, MemberDto memberDto, Long parentCommentId, String content, String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt) {
        return new ArticleCommentDto(id, articleId, memberDto, parentCommentId, content, createdBy, createdAt, modifiedBy, modifiedAt);
    }

    public static ArticleCommentDto from(ArticleComment entity) {
        return new ArticleCommentDto(
                entity.getId(),
                entity.getArticle().getId(),
                MemberDto.from(entity.getMember()),
                entity.getParentCommentId(),
                entity.getContent(),
                entity.getCreatedBy(),
                entity.getCreatedAt(),
                entity.getModifiedBy(),
                entity.getModifiedAt()
        );
    }

    public ArticleComment toEntity(Article article, Member member) {
        return ArticleComment.of(
                article,
                member,
                content
        );
    }
}
