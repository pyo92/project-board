package com.example.projectboard.dto.response;

import com.example.projectboard.domain.ArticleComment;
import com.example.projectboard.dto.ArticleCommentDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public record ArticleCommentResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        String userId,
        String email,
        String nickname,
        Long parentCommentId,
        Set<ArticleCommentResponse> childComments
) implements Serializable {

    public static ArticleCommentResponse of(Long id, String content, LocalDateTime createdAt, String userId, String email, String nickname) {
        return ArticleCommentResponse.of(id, content, createdAt, userId, email, nickname, null);
    }

    public static ArticleCommentResponse of(Long id, String content, LocalDateTime createdAt, String userId, String email, String nickname, Long parentCommentId) {
        Comparator<ArticleCommentResponse> childCommentComparator = Comparator
                .comparing(ArticleCommentResponse::createdAt)
                .thenComparingLong(ArticleCommentResponse::id);
        return new ArticleCommentResponse(id, content, createdAt, userId, email, nickname, parentCommentId, new TreeSet<>(childCommentComparator));
    }

    public static ArticleCommentResponse from(ArticleCommentDto dto) {
        String nickname = dto.memberDto().nickName();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.memberDto().userId();
        }

        return ArticleCommentResponse.of(
                dto.id(),
                dto.content(),
                dto.createdAt(),
                dto.memberDto().userId(),
                dto.memberDto().email(),
                nickname,
                dto.parentCommentId()
        );
    }

    public boolean hasParentComment() {
        return parentCommentId != null;
    }
}
