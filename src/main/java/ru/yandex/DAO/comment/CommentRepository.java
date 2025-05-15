package ru.yandex.DAO.comment;

public interface CommentRepository {
    void editComment(int commentId, String text);

    void createComment(int postId, String text);

    void deleteCommentById(int commentId);
}
