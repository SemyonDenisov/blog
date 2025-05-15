package ru.yandex.service.comment;

import org.springframework.stereotype.Service;
import ru.yandex.DAO.comment.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void editComment(int commentId, String text) {
        commentRepository.editComment(commentId, text);
    }

    @Override
    public void createComment(int postId, String text) {
        commentRepository.createComment(postId, text);
    }

    @Override
    public void deleteComment(int commentId) {
        commentRepository.deleteCommentById(commentId);
    }

}
