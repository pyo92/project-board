package com.example.projectboard.controller;

import com.example.projectboard.config.SecurityConfig;
import com.example.projectboard.dto.ArticleWithCommentsDto;
import com.example.projectboard.dto.MemberDto;
import com.example.projectboard.service.ArticleService;
import com.example.projectboard.service.PaginationService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View Controller - 게시글")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {
    
    private final MockMvc mvc;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private PaginationService paginationService;

    @Autowired
    public ArticleControllerTest(MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 게시판(게시글 리스트) 페이지 - 정상 호출")
    @Test
    void viewArticles() throws Exception {
        //given
        given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0, 1, 2, 3, 4));

        //when
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("paginationBarNumbers"));

        //then
        then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }

    //TODO: 게시판 페이지 pagination, sorting 테스트 코드 작성 (sorting 기능 개발은 다음 이슈때 진행)

    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상 호출")
    @Test
    void viewArticleDetail() throws Exception {
        //given
        Long articleId = 1L;
        given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentDto());

        //when
        mvc.perform(get("/articles/" + articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"));

        //then
        then(articleService).should().getArticle(articleId);
    }

    @Disabled("구현중")
    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출")
    @Test
    void viewArticleHashTagSearch() throws Exception {
        //given

        //when & then
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    private ArticleWithCommentsDto createArticleWithCommentDto() {
        return ArticleWithCommentsDto.of(
                1L,
                createMemberDto(),
                List.of(),
                "title",
                "content",
                "#spring",
                "pyo",
                LocalDateTime.now(),
                "pyo",
                LocalDateTime.now()
        );
    }

    private MemberDto createMemberDto() {
        return MemberDto.of(
                1L,
                "pyo",
                "password",
                "gipyopark@gmail.com",
                "pyo",
                "I am pyo.",
                "pyo",
                LocalDateTime.now(),
                "pyo",
                LocalDateTime.now()
        );
    }
}