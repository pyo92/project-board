package com.example.projectboard.repository;

import com.example.projectboard.config.JpaConfig;
import com.example.projectboard.domain.Article;
import com.example.projectboard.domain.Hashtag;
import com.example.projectboard.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA CRUD 테스트")
@Import(JpaRepositoryTest.TestJpaConfig.class) //Auditing
@DataJpaTest
class JpaRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

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
                .hasSize(123);
    }

    @DisplayName("insert 테스트")
    @Test
    void insert() {
        //given
        long previousArticleCount = articleRepository.count();
        Member member = memberRepository.save(Member.of("test", "test1234", "test@email.com", "test", ""));
        Article article = Article.of(member, "test", "test");
        article.addHashtags(Set.of(Hashtag.of("test")));

        //when
        articleRepository.save(article);

        //then
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount + 1);
    }

    @DisplayName("update 테스트")
    @Test
    void update() {
        //given
        Article article = articleRepository.findById(1L).orElseThrow();
        Hashtag updatedHashtag = Hashtag.of("test-updated");
        article.clearHashtags();
        article.addHashtags(Set.of(updatedHashtag));

        //when
        Article savedArticle = articleRepository.saveAndFlush(article);//flush -> update sql 발생

        //then
        assertThat(savedArticle.getHashtags())
                .hasSize(1)
                .extracting("name", String.class)
                .containsExactly(updatedHashtag.getName());
    }

    @DisplayName("delete 테스트")
    @Test
    void delete() {
        //given
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        int deletedCommentsSize = article.getArticleComments().size();

        //when
        articleRepository.delete(article);
        articleRepository.flush(); //flush -> delete sql 발생

        //then
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentsSize);
    }

    @EnableJpaAuditing
    @TestConfiguration
    public static class TestJpaConfig {
        @Bean
        public AuditorAware<String> auditorAware() {
            return () -> Optional.of("test");
        }
    }
}