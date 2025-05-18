package ru.yandex.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.DAO.comment.CommentRepository;
import ru.yandex.configuration.TestConfig;
import ru.yandex.service.comment.CommentService;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class CommentServiceUnitTest {
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void resetMocks() {
        reset(commentRepository);
    }

    @Test
    void testAddComment_success() {
        doNothing().when(commentRepository).createComment(0,"text");
        commentService.createComment(0,"text");
        verify(commentRepository, times(1)).createComment(0,"text");
    }
    @Test
    void testDeleteComment_success() {
        doNothing().when(commentRepository).deleteCommentById(0);
        commentService.deleteComment(0);
        verify(commentRepository, times(1)).deleteCommentById(0);
    }
    @Test
    void testEditComment_success() {
        doNothing().when(commentRepository).editComment(0,"text");
        commentService.editComment(0,"text");
        verify(commentRepository, times(1)).editComment(0,"text");
    }
}
