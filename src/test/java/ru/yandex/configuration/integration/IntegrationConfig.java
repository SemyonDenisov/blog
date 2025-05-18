package ru.yandex.configuration.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.DAO.comment.CommentRepository;
import ru.yandex.DAO.comment.JdbcNativeCommentRepository;
import ru.yandex.DAO.post.JdbcNativePostRepository;
import ru.yandex.DAO.post.PostRepository;
import ru.yandex.service.comment.CommentService;
import ru.yandex.service.comment.CommentServiceImpl;
import ru.yandex.service.post.PostService;
import ru.yandex.service.post.PostServiceImpl;

@Configuration
public class IntegrationConfig {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Bean
    public PostRepository postRepository() {
        return new JdbcNativePostRepository(jdbcTemplate);
    }

    @Bean
    public PostService postService(PostRepository postRepository) {
        return new PostServiceImpl(postRepository);
    }

    @Bean
    public CommentRepository commentRepository() {
        return new JdbcNativeCommentRepository(jdbcTemplate);
    }

    @Bean
    public CommentService commentService(CommentRepository commentRepository) {
        return new CommentServiceImpl(commentRepository);
    }
}