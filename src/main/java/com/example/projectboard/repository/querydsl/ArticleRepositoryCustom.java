package com.example.projectboard.repository.querydsl;

import com.example.projectboard.domain.Article;
import com.example.projectboard.domain.type.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface ArticleRepositoryCustom {

    Page<Article> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable);

    Page<Article> findBySearchKeywordAndHashtagNames(SearchType searchType, String searchKeyword, Collection<String> hashtagNames, Pageable pageable);
}
