package com.example.projectboard.service;

import com.example.projectboard.domain.Article;
import com.example.projectboard.domain.Hashtag;
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

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final MemberRepository memberRepository;

    private final HashtagService hashtagService;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, String filterTags, Pageable pageable) {
        if (filterTags != null) {
            filterTags = filterTags.replace("{", "").replace("}", "");
        }

        if ((searchKeyword == null || searchKeyword.isBlank()) && (filterTags == null || filterTags.isEmpty())) {
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }

        if (searchKeyword == null || searchKeyword.isBlank()) {
            //필터링 조회
            return articleRepository.findByHashtagNames(Set.of(filterTags.split(",")), pageable).map(ArticleDto::from);
        } else if (filterTags == null || filterTags.isEmpty()) {
            //검색바 조회
            return switch (searchType) {
                case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
                case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
                case NICKNAME -> articleRepository.findByMember_NickNameContaining(searchKeyword, pageable).map(ArticleDto::from);
            };
        } else {
            //검색바 + 필터링 조회
            return articleRepository.findBySearchKeywordAndHashtagNames(searchType, searchKeyword, Set.of(filterTags.split(",")), pageable).map(ArticleDto::from);
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

        Set<Hashtag> hashtags = renewHashtagsFromContent(dto.content());

        Article article = dto.toEntity(member);
        article.addHashtags(hashtags);
        articleRepository.save(article);
    }

    public void updateArticle(Long articleId, ArticleDto dto) {
        try {
            Article article = articleRepository.getReferenceById(articleId);
            Member member = memberRepository.getReferenceById(dto.memberDto().userId());

            if (article.getMember().equals(member)) {
                if (dto.title() != null) { article.setTitle(dto.title()); }
                if (dto.content() != null) { article.setContent(dto.content()); }

                Set<Long> oldHashtagIds = article.getHashtags().stream()
                        .map(Hashtag::getId)
                        .collect(Collectors.toUnmodifiableSet());

                article.clearHashtags();
                articleRepository.flush();

                oldHashtagIds.forEach(hashtagService::deleteHashtagWithoutArticles);

                Set<Hashtag> newHashtags = renewHashtagsFromContent(dto.content());
                article.addHashtags(newHashtags);
            }
            //save() 기재할 필요 없다.
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 게시글 수정에 필요한 정보를 찾을 수 없습니다.. - dto: {}", dto);
        }
    }

    public void deleteArticle(Long articleId, String userId) {
        Article article = articleRepository.getReferenceById(articleId);
        Set<Long> hashtagIds = article.getHashtags().stream()
                .map(Hashtag::getId)
                .collect(Collectors.toUnmodifiableSet());

        articleRepository.deleteByIdAndMember_UserId(articleId, userId);
        articleRepository.flush();

        hashtagIds.forEach(hashtagService::deleteHashtagWithoutArticles);
    }

    @Transactional(readOnly = true)
    public long getArticleCount() {
        return articleRepository.count();
    }

    private Set<Hashtag> renewHashtagsFromContent(String content) {
        Set<String> hashtagNamesInContent = hashtagService.parseHashtagNames(content);
        Set<Hashtag> hashtagInContent = hashtagService.findHashtagsByNames(hashtagNamesInContent);
        Set<String> existHashtagNameInContent = hashtagInContent.stream().map(Hashtag::getName).collect(Collectors.toUnmodifiableSet());

        hashtagNamesInContent.forEach(
                newHashtagNames -> {
                    if (!existHashtagNameInContent.contains(newHashtagNames)) {
                        hashtagInContent.add(Hashtag.of(newHashtagNames));
                    }
                }
        );

        return hashtagInContent;
    }
}