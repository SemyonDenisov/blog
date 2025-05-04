package ru.yandex.service.post;

import org.springframework.stereotype.Service;
import ru.yandex.model.Post;
import ru.yandex.paging.Paging;

import java.util.List;


public interface PostService {
    List<Post> findAll(int pageSize,int pageNumber);

    void save(Post post);

    void deleteById(int id);

    Post findById(int id);

    void like(int id, boolean decision);

    void editComment(int commentId, String text);

    void createComment(int postId, String text);

    void deleteComment(int commentId);

    void editPost(Post post);
    List<Post> findAllByTagOfDefault(String tag,int skip,int limit);

    Paging getPaging(String tag, int pageSize, int pageNumber);
}
