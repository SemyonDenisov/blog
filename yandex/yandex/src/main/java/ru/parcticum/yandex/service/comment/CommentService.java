package ru.parcticum.yandex.service.comment;

import ru.parcticum.yandex.model.Comment;

import java.util.List;

public interface CommentService {
    void editComment(int commentId, int postId, String text);

    void createComment(int postId, String text);

    void deleteComment(int commentId);

    List<Comment> findComments(int postId);
}
