package com.example.projectboard.dto.request;

import com.example.projectboard.dto.ArticleCommentDto;
import com.example.projectboard.dto.MemberDto;

public record ArticleCommentRequest(

        Long articleId,
        String content
) {

    public static ArticleCommentRequest of(Long articleId, String content) {
        return new ArticleCommentRequest(articleId, content);
    }

    public ArticleCommentDto toDto(MemberDto memberDto) {
        return ArticleCommentDto.of(articleId, memberDto, content);
    }
}
