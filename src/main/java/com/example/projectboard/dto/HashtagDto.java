package com.example.projectboard.dto;

import com.example.projectboard.domain.Hashtag;

import java.time.LocalDateTime;

public record HashtagDto(
        Long id,
        String name,
        String createdBy,
        LocalDateTime createdAt,
        String modifiedBy,
        LocalDateTime modifiedAt
) {

    public static HashtagDto of(String name) {
        return new HashtagDto(null, name, null, null, null, null);
    }

    public static HashtagDto of(Long id, String name, String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt) {
        return new HashtagDto(id, name, createdBy, createdAt, modifiedBy, modifiedAt);
    }

    public static HashtagDto from(Hashtag entity) {
        return new HashtagDto(
                entity.getId(),
                entity.getName(),
                entity.getCreatedBy(),
                entity.getCreatedAt(),
                entity.getModifiedBy(),
                entity.getModifiedAt()
        );
    }

    public Hashtag toEntity() {
        return Hashtag.of(name);
    }
}
