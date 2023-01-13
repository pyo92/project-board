package com.example.projectboard.controller;

import com.example.projectboard.dto.MemberDto;
import com.example.projectboard.dto.request.ArticleCommentRequest;
import com.example.projectboard.dto.security.BoardPrincipal;
import com.example.projectboard.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/comments")
@Controller
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;

    @PostMapping("/new")
    public String newComment(ArticleCommentRequest request, @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        articleCommentService.saveArticleComment(request.toDto(boardPrincipal.toDto()));
        return "redirect:/articles/" + request.articleId();
    }

    @PostMapping("/{articleCommentId}/delete")
    public String deleteComment(@PathVariable Long articleCommentId, Long articleId, @AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        articleCommentService.deleteArticleComment(articleCommentId, boardPrincipal.getUsername());
        return "redirect:/articles/" + articleId;
    }
}
