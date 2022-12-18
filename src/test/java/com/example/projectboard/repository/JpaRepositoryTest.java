package com.example.projectboard.repository;

import com.example.projectboard.config.JpaConfig;
import com.example.projectboard.domain.Article;
import com.example.projectboard.domain.ArticleComment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA CRUD 테스트")
@Import(JpaConfig.class) //Auditing
@DataJpaTest
class JpaRepositoryTest {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ArticleCommentRepository articleCommentRepository;

    @DisplayName("select 테스트")
    @Test
    void select() {
        //given

        //when
        List<Article> articles = articleRepository.findAll();

        //then
        assertThat(articles)
                .isNotNull()
                .hasSize(0);
    }

    @DisplayName("insert 테스트")
    @Test
    void insert() {
        //given
        Article article = Article.of("test article", "test article's content", "#spring");
        ArticleComment comment = ArticleComment.of(article, "test articles' comment");
        article.getArticleComments().add(comment);

        //when
        articleRepository.saveAndFlush(article);

        //then
        assertThat(articleRepository.findAll())
                .isNotNull()
                .hasSize(1);

        assertThat(articleCommentRepository.findAll())
                .isNotNull()
                .hasSize(1);
    }

    @DisplayName("update 테스트")
    @Test
    void update() {
        //given
        Article article = Article.of("test article", "test article's content", "#spring");
        ArticleComment comment = ArticleComment.of(article, "test articles' comment");
        article.getArticleComments().add(comment);

        articleRepository.saveAndFlush(article);

        //when
        Article savedArticle = articleRepository.findAll().get(0);
        savedArticle.setHashTag("#springboot");

        for (ArticleComment c : savedArticle.getArticleComments()) {
            c.setContent("updated test articles' comment");
        }

        articleRepository.flush(); //flush -> update sql 발생

        //then
        Article findedArticle = articleRepository.findAll().get(0);
        assertThat(findedArticle.getHashTag())
                .isEqualTo("#springboot");

        for (ArticleComment c : findedArticle.getArticleComments()) {
            assertThat(c.getContent())
                    .isEqualTo("updated test articles' comment");
        }
    }

    @DisplayName("delete 테스트")
    @Test
    void delete() {
        //given
        Article article = Article.of("test article", "test article's content", "#spring");
        ArticleComment comment = ArticleComment.of(article, "test articles' comment");
        article.getArticleComments().add(comment);

        articleRepository.saveAndFlush(article);

        //when
        Article findedArticle = articleRepository.findAll().get(0);
        articleRepository.delete(findedArticle);

        articleRepository.flush(); //flush -> delete sql 발생

        //then
        assertThat(articleRepository.findAll())
                .isNotNull()
                .hasSize(0);

        assertThat(articleCommentRepository.findAll())
                .isNotNull()
                .hasSize(0); //cascade
    }

}