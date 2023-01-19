package com.example.projectboard.repository;

import com.example.projectboard.domain.Article;
import com.example.projectboard.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Set;


@RepositoryRestResource
public interface HashtagRepository extends
        JpaRepository<Hashtag, Long>
        , QuerydslPredicateExecutor<Article> {

    Set<Hashtag> findByNameIn(Set<String> name);
}
