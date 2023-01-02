package com.example.projectboard.domain.type;

import lombok.Getter;

public enum FormType {

    CREATE("작성", false),
    UPDATE("수정", true);

    @Getter
    private final String description;

    @Getter
    private final Boolean isUpdate;

    FormType(String description, Boolean isUpdate) {
        this.description = description;
        this.isUpdate = isUpdate;
    }
}
