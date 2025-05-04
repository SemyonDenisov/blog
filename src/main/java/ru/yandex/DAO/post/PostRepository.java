package ru.yandex.DAO.post;

import ru.yandex.model.Post;
import ru.yandex.paging.Paging;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    List<Post> findAll(int skip,int limit);

    void save(Post post);

    void deleteById(int id);

    Optional<Post> findById(int id);

    void updateLikesCount(int id, boolean decision);

    void editComment(int commentId, String text);

    void createComment(int postId, String text);

    void deleteCommentById(int commentId);

    List<Post> findAllByTagOfDefault(String tag,int pageSize,int pageNumber);

    Paging getPaging(String tag, int pageSize, int pageNumber);
}

