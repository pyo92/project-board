package com.example.projectboard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "userId"),
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createAt"),
        @Index(columnList = "createBy")
})
@Entity
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @Setter
    @Column(nullable = false, length = 100)
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

    protected Member() {};

    private Member(Long id, String userId, String userPw, String email, String nickName) {
        this.id = id;
        this.userId = userId;
        this.userPw = userPw;
        this.email = email;
        this.nickName = nickName;
    }

    public static Member of(Long id, String userId, String userPw, String email, String nickName) {
        return new Member(id, userId, userPw, email, nickName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member member)) return false;
        return id != null & Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
