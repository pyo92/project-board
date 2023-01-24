package com.example.projectboard.service;

import com.example.projectboard.domain.Article;
import com.example.projectboard.domain.ArticleComment;
import com.example.projectboard.domain.Member;
import com.example.projectboard.dto.ArticleCommentDto;
import com.example.projectboard.dto.MemberDto;
import com.example.projectboard.repository.ArticleCommentRepository;
import com.example.projectboard.repository.ArticleRepository;
import com.example.projectboard.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {

    @InjectMocks
    private ArticleCommentService sut;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ArticleCommentRepository articleCommentRepository;

    @Mock
    private MemberRepository memberRepository;

    ArticleCommentServiceTest() {
    }
    
    @DisplayName("댓글 정보를 입력하면, 댓글을 저장한다.")
    @Test
    void givenArticleCommentInfo_whenSavingArticleComment_thenSavesArticleComment() {
        // Given
        ArticleCommentDto dto = createArticleCommentDto("댓글");
        given(articleRepository.getReferenceById(dto.articleId())).willReturn(createArticle());
        given(memberRepository.getReferenceById(dto.memberDto().userId())).willReturn(createMember());
        given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);

        // When
        sut.saveArticleComment(dto);

        // Then
        then(articleRepository).should().getReferenceById(dto.articleId());
        then(memberRepository).should().getReferenceById(dto.memberDto().userId());
        then(articleCommentRepository).should().save(any(ArticleComment.class));
    }

    @DisplayName("댓글 저장을 시도했는데 맞는 게시글이 없으면, 경고 로그를 찍고 아무것도 안 한다.")
    @Test
    void givenNonexistentArticle_whenSavingArticleComment_thenLogsSituationAndDoesNothing() {
        // Given
        ArticleCommentDto dto = createArticleCommentDto("댓글");
        given(articleRepository.getReferenceById(dto.articleId())).willThrow(EntityNotFoundException.class);

        // When
        sut.saveArticleComment(dto);

        // Then
        then(articleRepository).should().getReferenceById(dto.articleId());
        then(memberRepository).shouldHaveNoInteractions();
        then(articleCommentRepository).shouldHaveNoInteractions();
    }

    @DisplayName("부모 댓글 ID와 댓글 정보를 입력하면, 대댓글을 저장한다.")
    @Test
    void givenParentCommentIdAndArticleCommentInfo_whenSaving_thenSavesChildComment() {
        // Given
        Long parentCommentId = 1L;
        ArticleCommentDto parent = createArticleCommentDto(parentCommentId, "댓글");
        ArticleCommentDto child = createArticleCommentDto(parentCommentId, "대댓글");
        given(articleRepository.getReferenceById(child.articleId())).willReturn(createArticle());
        given(memberRepository.getReferenceById(child.memberDto().userId())).willReturn(createMember());
        given(articleCommentRepository.getReferenceById(child.parentCommentId())).willReturn(parent.toEntity(createArticle(), createMember()));

        // When
        sut.saveArticleComment(child);

        // Then
        assertThat(child.parentCommentId()).isNotNull();
        then(articleRepository).should().getReferenceById(child.articleId());
        then(memberRepository).should().getReferenceById(child.memberDto().userId());
        then(articleCommentRepository).should().getReferenceById(child.parentCommentId());
        then(articleCommentRepository).should(never()).save(any(ArticleComment.class));
    }

    @DisplayName("댓글 ID를 입력하면, 댓글을 삭제한다.")
    @Test
    void givenArticleCommentId_whenDeletingArticleComment_thenDeletesArticleComment() {
        // Given
        Long articleCommentId = 1L;
        String userId = "test";
        willDoNothing().given(articleCommentRepository).deleteByIdAndMember_UserId(articleCommentId, userId);

        // When
        sut.deleteArticleComment(articleCommentId, userId);

        // Then
        then(articleCommentRepository).should().deleteByIdAndMember_UserId(articleCommentId, userId);
    }

    private ArticleCommentDto createArticleCommentDto(String content) {
        return createArticleCommentDto(null, content);
    }

    private ArticleCommentDto createArticleCommentDto(Long parentCommentId, String content) {
        return ArticleCommentDto.of(
                1L,
                1L,
                createMemberDto(),
                parentCommentId,
                content,
                "pyo",
                LocalDateTime.now(),
                "pyo",
                LocalDateTime.now()
        );
    }

    private MemberDto createMemberDto() {
        return MemberDto.of(
                "pyo",
                "pyo1234",
                "gipyopark@gmail.com",
                "pyo",
                "I am pyo.",
                "pyo",
                LocalDateTime.now(),
                "pyo",
                LocalDateTime.now()
        );
    }

    private Member createMember() {
        return Member.of(
                "pyo",
                "pyo1234",
                "giyopark@gmail.com",
                "pyo",
                "I am pyo."
        );
    }

    private Article createArticle() {
        return Article.of(
                createMember(),
                "title",
                "content"
        );
    }

    private ArticleComment createArticleComment(Long id, String content) {
        ArticleComment articleComment = ArticleComment.of(
                createArticle(),
                createMember(),
                content
        );

        ReflectionTestUtils.setField(articleComment, "id", id);

        return articleComment;
    }
}
