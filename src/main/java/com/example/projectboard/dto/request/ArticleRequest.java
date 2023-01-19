package com.example.projectboard.dto.request;

import com.example.projectboard.dto.ArticleDto;
import com.example.projectboard.dto.HashtagDto;
import com.example.projectboard.dto.MemberDto;

import java.util.Set;

public record ArticleRequest(
        String title,
        String content
) {

    public static ArticleRequest of(String title, String content) {
        return new ArticleRequest(title, content);
    }

    public ArticleDto toDto(MemberDto memberDto) {
        return toDto(memberDto, null);
    }

    public ArticleDto toDto(MemberDto memberDto, Set<HashtagDto> hashtagDtos) {
        return ArticleDto.of(memberDto, title, content, hashtagDtos);
    }
}
