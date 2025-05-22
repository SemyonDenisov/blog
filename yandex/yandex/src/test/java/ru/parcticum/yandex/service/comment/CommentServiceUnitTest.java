package ru.parcticum.yandex.service.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.parcticum.yandex.DAO.comment.CommentRepository;
import ru.parcticum.yandex.model.Comment;

import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {})
public class CommentServiceUnitTest {

    @MockitoBean
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @BeforeEach
    void resetMocks() {
        reset(commentRepository);
    }

    @Test
    void testAddComment_success() {
        when(commentRepository.save(any(Comment.class))).thenReturn(new Comment(1,"text"));
        commentService.createComment(1,"text");
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testDeleteComment_success() {
        doNothing().when(commentRepository).deleteCommentById(0);
        commentService.deleteComment(0);
        verify(commentRepository, times(1)).deleteCommentById(0);
    }

    @Test
    void testEditComment_success() {
        when(commentRepository.save(any(Comment.class))).thenReturn(new Comment(1,"text"));
        commentService.editComment(0, 1,"text");
        verify(commentRepository, times(1)).save(any(Comment.class));
    }
}
