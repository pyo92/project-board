package com.example.projectboard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class Member extends AuditingFields {

    @Column(nullable = false, length = 100)
    @Id
    private String userId;

    @Setter
    @Column(nullable = false)
    private String userPw;

    @Setter
    @Column(length = 100)
    private String email;

    @Setter
    @Column(length = 100)
    private String nickName;

    @Setter
    @Column
    private String memo;

    protected Member() {};

    private Member(String userId, String userPw, String email, String nickName, String memo) {
        this.userId = userId;
        this.userPw = userPw;
        this.email = email;
        this.nickName = nickName;
        this.memo = memo;
    }

    public static Member of(String userId, String userPw, String email, String nickName, String memo) {
        return new Member(userId, userPw, email, nickName, memo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member that)) return false;
        return this.getUserId() != null && this.getUserId().equals(that.getUserId());
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.getUserId());
    }
}
