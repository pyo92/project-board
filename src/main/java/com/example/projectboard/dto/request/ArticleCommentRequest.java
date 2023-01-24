package com.example.projectboard.dto.request;

import com.example.projectboard.dto.ArticleCommentDto;
import com.example.projectboard.dto.MemberDto;

public record ArticleCommentRequest(

        Long articleId,
        Long parentCommentId,
        String content
) {

    public static ArticleCommentRequest of(Long articleId, String content) {
        return ArticleCommentRequest.of(articleId, null, content);
    }

    public static ArticleCommentRequest of(Long articleId, Long parentCommentId, String content) {
        return new ArticleCommentRequest(articleId, parentCommentId, content);
    }

    public ArticleCommentDto toDto(MemberDto memberDto) {
        return ArticleCommentDto.of(
                articleId,
                memberDto,
                parentCommentId,
                content);
    }
}
