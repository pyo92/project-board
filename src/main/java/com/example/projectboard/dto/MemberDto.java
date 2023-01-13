package com.example.projectboard.dto;

import com.example.projectboard.domain.Member;

import java.time.LocalDateTime;

public record MemberDto(
        String userId,
        String userPw,
        String email,
        String nickName,
        String memo,
        String createdBy,
        LocalDateTime createdAt,
        String modifiedBy,
        LocalDateTime modifiedAt

) {
    public static MemberDto of(String userId, String userPw, String email, String nickName, String memo, String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt) {
        return new MemberDto(userId, userPw, email, nickName, memo, createdBy, createdAt, modifiedBy, modifiedAt);
    }

    public static MemberDto from(Member entity) {
        return new MemberDto(
                entity.getUserId(),
                entity.getUserPw(),
                entity.getEmail(),
                entity.getNickName(),
                entity.getMemo(),
                entity.getCreatedBy(),
                entity.getCreatedAt(),
                entity.getModifiedBy(),
                entity.getModifiedAt()
        );
    }

    public Member toEntity() {
        return Member.of(
                userId,
                userPw,
                email,
                nickName,
                memo
        );
    }
}
