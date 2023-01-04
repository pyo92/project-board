package com.example.projectboard.dto.request;

import com.example.projectboard.dto.ArticleDto;
import com.example.projectboard.dto.MemberDto;

public record ArticleRequest(
        String title,
        String content,
        String hashtag
) {

    public static ArticleRequest of(String title, String content, String hashtag) {
        return new ArticleRequest(title, content, hashtag);
    }

    public ArticleDto toDto(MemberDto memberDto) {
        return ArticleDto.of(memberDto, title, content, hashtag);
    }
}
