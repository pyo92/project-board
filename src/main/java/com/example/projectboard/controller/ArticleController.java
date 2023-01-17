package com.example.projectboard.controller;

import com.example.projectboard.domain.type.FormType;
import com.example.projectboard.domain.type.SearchType;
import com.example.projectboard.dto.MemberDto;
import com.example.projectboard.dto.request.ArticleRequest;
import com.example.projectboard.dto.response.ArticleResponse;
import com.example.projectboard.dto.response.ArticleWithCommentResponse;
import com.example.projectboard.dto.security.BoardPrincipal;
import com.example.projectboard.service.ArticleService;
import com.example.projectboard.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/articles")
@Controller
public class ArticleController {

    private final ArticleService articleService;

    private final PaginationService paginationService;

    @GetMapping
    public String articles(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchValue,
            @RequestParam(required = false) List<String> filterTags,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map
    ) {
        Page<ArticleResponse> articles = articleService.searchArticles(searchType, searchValue, filterTags, pageable).map(ArticleResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages());
        map.addAttribute("articles", articles);
        map.addAttribute("paginationBarNumbers", barNumbers);
        map.addAttribute("searchTypes", SearchType.values());
        map.addAttribute("hashtags", articleService.getAllHashTag(searchType, searchValue)); //hashtag 목록

        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String article(
            @PathVariable Long articleId,
            ModelMap map
    ) {
        ArticleWithCommentResponse articleWithCommentResponse = ArticleWithCommentResponse.from(articleService.getArticleWithCommentsDto(articleId));

        map.addAttribute("article", articleWithCommentResponse);
        map.addAttribute("articleComments", articleWithCommentResponse.articleCommentsResponse());
        map.addAttribute("totalCount", articleService.getArticleCount());
        return "articles/detail";
    }

    @GetMapping("/form")
    public String newArticleForm(ModelMap map) {
        map.addAttribute("formType", FormType.CREATE);
        return "articles/form";
    }

    @PostMapping("/form")
    public String newArticle(ArticleRequest articleRequest, @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        articleService.saveArticle(articleRequest.toDto(boardPrincipal.toDto()));
        return "redirect:/articles";
    }

    @GetMapping("/{articleId}/form")
    public String updateArticleForm(@PathVariable Long articleId, ModelMap map) {
        map.addAttribute("formType", FormType.UPDATE);
        map.addAttribute("article", ArticleResponse.from(articleService.getArticle(articleId)));
        return "articles/form";
    }

    @PostMapping("/{articleId}/form")
    public String updateArticle(@PathVariable Long articleId, ArticleRequest articleRequest, @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        articleService.updateArticle(articleId, articleRequest.toDto(boardPrincipal.toDto()));
        return "redirect:/articles/" + articleId;
    }

    @PostMapping("/{articleId}/delete")
    public String deleteArticle(@PathVariable Long articleId, @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        articleService.deleteArticle(articleId, boardPrincipal.getUsername());
        return "redirect:/articles";
    }
}