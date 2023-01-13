package com.example.projectboard.repository;

import com.example.projectboard.domain.Article;
import com.example.projectboard.domain.QArticle;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>
        , QuerydslPredicateExecutor<Article>
        , QuerydslBinderCustomizer<QArticle> {


    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByContentContaining(String content, Pageable pageable);
    Page<Article> findByMember_NickNameContaining(String nickName, Pageable pageable);

    Page<Article> findByTitleContainingAndHashTagIn(String title, List<String> filterTags, Pageable pageable);
    Page<Article> findByContentContainingAndHashTagIn(String content, List<String> filterTags, Pageable pageable);
    Page<Article> findByMember_NickNameContainingAndHashTagIn(String nickName, List<String> filterTags, Pageable pageable);

    Page<Article> findByHashTagIn(List<String> filterTags, Pageable pageable);

    @Query(value = "select distinct a.hashTag from Article a where a.hashTag is not null")
    List<String> findAllHashTag();

    void deleteByIdAndMember_UserId(Long id, String userId);

    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title, root.hashTag, root.createdBy);
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); //like '%{v}%'
        bindings.bind(root.hashTag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
}