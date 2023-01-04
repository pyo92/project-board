package com.example.projectboard.dto;

import com.example.projectboard.domain.Article;
import com.example.projectboard.domain.ArticleComment;
import com.example.projectboard.domain.Member;

import java.time.LocalDateTime;

public record ArticleCommentDto(
        Long id,
        Long articleId,
        MemberDto memberDto,
        String content,
        String createdBy,
        LocalDateTime createdAt,
        String modifiedBy,
        LocalDateTime modifiedAt
) {

    public static ArticleCommentDto of(Long articleId, MemberDto memberDto, String content) {
        return new ArticleCommentDto(null, articleId, memberDto, content, null, null, null, null);
    }

    public static ArticleCommentDto of(Long id, Long articleId, MemberDto memberDto, String content, String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt) {
        return new ArticleCommentDto(id, articleId, memberDto, content, createdBy, createdAt, modifiedBy, modifiedAt);
    }

    public static ArticleCommentDto from(ArticleComment entity) {
        return new ArticleCommentDto(
                entity.getId(),
                entity.getArticle().getId(),
                MemberDto.from(entity.getMember()),
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
