package ru.yandex.service.comment;

public interface CommentService {
    void editComment(int commentId, String text);

    void createComment(int postId, String text);

    void deleteComment(int commentId);
}
