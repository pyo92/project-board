package com.example.projectboard.service;

import com.example.projectboard.domain.Article;
import com.example.projectboard.domain.Member;
import com.example.projectboard.domain.type.SearchType;
import com.example.projectboard.dto.ArticleDto;
import com.example.projectboard.dto.ArticleWithCommentsDto;
import com.example.projectboard.repository.ArticleRepository;
import com.example.projectboard.repository.MemberRepository;
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

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, List<String> filterTags, Pageable pageable) {
        if ((searchKeyword == null || searchKeyword.isBlank()) && (filterTags == null || filterTags.isEmpty())) {
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }

        if (searchKeyword == null || searchKeyword.isBlank()) {
            //필터링 조회
            return articleRepository.findByHashTagIn(filterTags, pageable).map(ArticleDto::from);
        }

        if (filterTags == null || filterTags.isEmpty()) {
            //검색바 조회
            return switch (searchType) {
                case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
                case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
                case NICKNAME -> articleRepository.findByMember_NickNameContaining(searchKeyword, pageable).map(ArticleDto::from);
            };
        } else {
            //검색바 + 필터링 조회
            return switch (searchType) {
                case TITLE -> articleRepository.findByTitleContainingAndHashTagIn(searchKeyword, filterTags, pageable).map(ArticleDto::from);
                case CONTENT -> articleRepository.findByContentContainingAndHashTagIn(searchKeyword, filterTags, pageable).map(ArticleDto::from);
                case NICKNAME -> articleRepository.findByMember_NickNameContainingAndHashTagIn(searchKeyword, filterTags, pageable).map(ArticleDto::from);
            };
        }
    }

    @Transactional(readOnly = true)
    public ArticleDto getArticle(long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다. (articleId: " + articleId + ")"));
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticleWithCommentsDto(long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다. (articleId: " + articleId + ")"));
    }

    public void saveArticle(ArticleDto dto) {
        Member member = memberRepository.getReferenceById(dto.memberDto().userId());
        articleRepository.save(dto.toEntity(member));
    }

    public void updateArticle(Long articleId, ArticleDto dto) {
        try {
            Article article = articleRepository.getReferenceById(articleId);
            Member member = memberRepository.getReferenceById(dto.memberDto().userId());

            if (article.getMember().equals(member)) {
                if (dto.title() != null) { article.setTitle(dto.title()); }
                if (dto.title() != null) { article.setContent(dto.content()); }
                article.setHashTag(dto.hashTag());
            }
            //save() 기재할 필요 없다.
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 게시글 수정에 필요한 정보를 찾을 수 없ㅅ브니다.. - dto: {}", dto);
        }
    }

    public void deleteArticle(Long articleId, String userId) {
        articleRepository.deleteByIdAndMember_UserId(articleId, userId);
    }

    @Transactional(readOnly = true)
    public long getArticleCount() {
        return articleRepository.count();
    }

    @Transactional(readOnly = true)
    public List<String> getAllHashTag() {
        return articleRepository.findAllHashTag();
    }
}