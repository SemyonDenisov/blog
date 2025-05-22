package ru.parcticum.yandex.service.comment;

import org.springframework.stereotype.Service;
import ru.parcticum.yandex.DAO.comment.CommentRepository;
import ru.parcticum.yandex.model.Comment;
import ru.parcticum.yandex.tools.InputValidator;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void editComment(int commentId,int postId, String text) {
        if (InputValidator.isValidComment(text)) {
            commentRepository.save(new Comment(commentId,postId, text));
        }
    }

    @Override
    public void createComment(int postId, String text) {
        if (InputValidator.isValidComment(text)) {
            commentRepository.save(new Comment(null, postId, text));
        }
    }

    @Override
    public void deleteComment(int commentId) {
        commentRepository.deleteCommentById(commentId);
    }

    @Override
    public List<Comment> findComments(int postId) {
        return commentRepository.findAllByPostId(postId);
    }
}
