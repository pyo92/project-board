package com.example.projectboard.repository.querydsl;

import com.example.projectboard.domain.Article;
import com.example.projectboard.domain.QArticle;
import com.example.projectboard.domain.QHashtag;
import com.example.projectboard.domain.type.SearchType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Collection;
import java.util.List;

public class ArticleRepositoryCustomImpl extends QuerydslRepositorySupport implements ArticleRepositoryCustom {

    public ArticleRepositoryCustomImpl() {
        super(Article.class);
    }

    @Override
    public Page<Article> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable) {
        QArticle article = QArticle.article;
        QHashtag hashtag = QHashtag.hashtag;

        JPQLQuery<Article> query = from(article)
                .innerJoin(article.hashtags, hashtag)
                .where(hashtag.name.in(hashtagNames));

        List<Article> articles = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(articles, pageable, query.fetchCount());
    }

    @Override
    public Page<Article> findBySearchKeywordAndHashtagNames(SearchType searchType, String searchKeyword, Collection<String> hashtagNames, Pageable pageable) {
        QArticle article = QArticle.article;
        QHashtag hashtag = QHashtag.hashtag;

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        switch (searchType) {
            case TITLE -> booleanBuilder.and(article.title.containsIgnoreCase(searchKeyword));
            case CONTENT -> booleanBuilder.and(article.content.containsIgnoreCase(searchKeyword));
            case NICKNAME -> booleanBuilder.and(article.member.nickName.containsIgnoreCase(searchKeyword));
        }

        JPQLQuery<Article> query = from(article)
                .innerJoin(article.hashtags, hashtag)
                .where(booleanBuilder
                        .and(hashtag.name.in(hashtagNames)));

        List<Article> articles = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(articles, pageable, query.fetchCount());
    }
}
