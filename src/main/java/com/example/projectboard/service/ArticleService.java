package com.example.projectboard.service;

import com.example.projectboard.domain.Article;
import com.example.projectboard.domain.type.SearchType;
import com.example.projectboard.dto.ArticleDto;
import com.example.projectboard.dto.ArticleWithCommentsDto;
import com.example.projectboard.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        if (searchKeyword == null || searchKeyword.isBlank()) {
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }

        return switch (searchType) {
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
            case HASHTAG -> articleRepository.findByHashTag(searchKeyword, pageable).map(ArticleDto::from);
            case ID -> articleRepository.findByMember_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByMember_NickNameContaining(searchKeyword, pageable).map(ArticleDto::from);
        };
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticle(long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다. (articleId: " + articleId + ")"));
    }

    public void saveArticle(ArticleDto dto) {
        articleRepository.save(dto.toEntity());
    }

    public void updateArticle(ArticleDto dto) {
        try {
            Article article = articleRepository.getReferenceById(dto.id());
            if (dto.title() != null) { article.setTitle(dto.title()); }
            if (dto.title() != null) { article.setContent(dto.content()); }
            article.setHashTag(dto.hashTag());
            //save() 기재할 필요 없다.
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다. - dto: {}", dto);
        }
    }

    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }

    @Transactional(readOnly = true)
    public List<String> getAllHashTag() {
        return articleRepository.findAllHashTag();
    }
}
