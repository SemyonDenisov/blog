package ru.yandex.configuration.unit;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.DAO.comment.CommentRepository;
import ru.yandex.service.comment.CommentService;
import ru.yandex.service.comment.CommentServiceImpl;

@Configuration
public class UnitCommentConfig {

    @Bean
    public CommentRepository commentRepository() {
        return Mockito.mock(CommentRepository.class);
    }

    @Bean
    public CommentService commentService(CommentRepository commentRepository) {
        return new CommentServiceImpl(commentRepository);
    }
}
