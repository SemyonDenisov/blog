package ru.parcticum.yandex.DAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.parcticum.yandex.DAO.comment.CommentRepository;
import ru.parcticum.yandex.model.Comment;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
public class CommentRepositoryUnitTest {

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    public void setUp() {
        commentRepository.deleteAll();
    }

    @Test
    public void testSaveComment() {
        Comment comment = new Comment(null, "title");
        Comment savedComment = commentRepository.save(comment);
        assertThat(savedComment.getText()).isEqualTo(comment.getText());
    }

    @Test
    public void testDeleteComment() {
        Comment comment = new Comment(null, "title");
        Comment savedComment = commentRepository.save(comment);
        assertThat(commentRepository.findAll().size()).isEqualTo(1);
        commentRepository.deleteById(savedComment.getId());
        assertThat(commentRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    public void testFindComment() {
        Comment comment = new Comment(null, "title");
        Comment savedComment = commentRepository.save(comment);
        assertThat(commentRepository.findAll().size()).isEqualTo(1);
        Comment foundedComment = commentRepository.findById(savedComment.getId()).get();
        assertThat(foundedComment).isNotEqualTo(null);
        assertThat(foundedComment.getText()).isEqualTo(comment.getText());
    }

}