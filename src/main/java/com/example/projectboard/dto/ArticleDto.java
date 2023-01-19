package com.example.projectboard.dto;

import com.example.projectboard.domain.Article;
import com.example.projectboard.domain.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleDto(
        Long id,
        MemberDto memberDto,
        String title,
        String content,
        Set<HashtagDto> hashTagDtos,
        String createdBy,
        LocalDateTime createdAt,
        String modifiedBy,
        LocalDateTime modifiedAt
) {

    public static ArticleDto of(MemberDto memberDto, String title, String content, Set<HashtagDto> hashtagDtos) {
        return new ArticleDto(null, memberDto, title, content, hashtagDtos, null, null, null, null);
    }

    public static ArticleDto of(Long id, MemberDto memberDto, String title, String content, Set<HashtagDto> hashtagDtos, String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt) {
        return new ArticleDto(id, memberDto, title, content, hashtagDtos, createdBy, createdAt, modifiedBy, modifiedAt);
    }

    public static ArticleDto from(Article entity) {
        return new ArticleDto(
                entity.getId(),
                MemberDto.from(entity.getMember()),
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

    public Article toEntity(Member member) {
        return Article.of(
                member,
                title,
                content
        );
    }
}
