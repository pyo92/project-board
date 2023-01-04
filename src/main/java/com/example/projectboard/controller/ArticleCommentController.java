package com.example.projectboard.controller;

import com.example.projectboard.dto.MemberDto;
import com.example.projectboard.dto.request.ArticleCommentRequest;
import com.example.projectboard.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
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
    public String newComment(ArticleCommentRequest request) {
        //TODO: 추후 사용자 인증 정보를 넣어줘야 한다.
        MemberDto memberDto = MemberDto.of(1L, "pyo", "pyo1234", "gipyopark@gmail.com", "pyo", "I am pyo.", null, null, null, null);
        articleCommentService.saveArticleComment(request.toDto(memberDto));
        return "redirect:/articles/" + request.articleId();
    }

    @PostMapping("/{articleCommentId}/delete")
    public String deleteComment(@PathVariable Long articleCommentId, Long articleId) {
        articleCommentService.deleteArticleComment(articleCommentId);
        return "redirect:/articles/" + articleId;
    }
}
