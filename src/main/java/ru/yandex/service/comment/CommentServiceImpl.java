package ru.yandex.service.comment;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.DAO.comment.CommentRepository;
import ru.yandex.tools.InputValidator;

@Service
@Primary
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void editComment(int commentId, String text) {
        if(InputValidator.isValidComment(text)) {
            commentRepository.editComment(commentId, text);
        }
    }

    @Override
    public void createComment(int postId, String text) {
        if(InputValidator.isValidComment(text)) {
            commentRepository.createComment(postId, text);
        }
    }

    @Override
    public void deleteComment(int commentId) {
        commentRepository.deleteCommentById(commentId);
    }

}
