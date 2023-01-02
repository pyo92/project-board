package com.example.projectboard.dto;

import com.example.projectboard.domain.Article;
import com.example.projectboard.domain.Member;

import java.time.LocalDateTime;

public record ArticleDto(
        Long id,
        MemberDto memberDto,
        String title,
        String content,
        String hashTag,
        String createdBy,
        LocalDateTime createdAt,
        String modifiedBy,
        LocalDateTime modifiedAt
) {

    public static ArticleDto of(MemberDto memberDto, String title, String content, String hashTag) {
        return new ArticleDto(null, memberDto, title, content, hashTag, null, null, null, null);
    }

    public static ArticleDto of(Long id, MemberDto memberDto, String title, String content, String hashTag, String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt) {
        return new ArticleDto(id, memberDto, title, content, hashTag, createdBy, createdAt, modifiedBy, modifiedAt);
    }

    public static ArticleDto from(Article entity) {
        return new ArticleDto(
                entity.getId(),
                MemberDto.from(entity.getMember()),
                entity.getTitle(),
                entity.getContent(),
                entity.getHashTag(),
                entity.getCreatedBy(),
                entity.getCreatedAt(),
                entity.getModifiedBy(),
                entity.getModifiedAt()
        );
    }

    public Article toEntity(Member member) {
        return Article.of(
                member,
                title,
                content,
                hashTag
        );
    }
}
