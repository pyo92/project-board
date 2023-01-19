package com.example.projectboard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "name", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class Hashtag extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @OrderBy
    @Column(length = 50, nullable = false)
    private String name;

    @ToString.Exclude
    @ManyToMany(mappedBy = "hashtags")
    private List<Article> articles = new ArrayList<>();

    protected Hashtag() {}

    private Hashtag(String name) {
        this.name = name;
    }

    public static Hashtag of(String name) {
        return new Hashtag(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hashtag that)) return false;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
