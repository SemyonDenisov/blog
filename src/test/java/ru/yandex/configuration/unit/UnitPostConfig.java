package ru.yandex.configuration.unit;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.DAO.comment.CommentRepository;
import ru.yandex.DAO.post.PostRepository;
import ru.yandex.service.comment.CommentService;
import ru.yandex.service.comment.CommentServiceImpl;
import ru.yandex.service.post.PostService;
import ru.yandex.service.post.PostServiceImpl;

@Configuration
public class UnitPostConfig {

    @Bean
    public PostRepository postRepository() {
        return Mockito.mock(PostRepository.class);
    }

    @Bean
    public PostService postService(PostRepository postRepository) {
        return new PostServiceImpl(postRepository);
    }
}