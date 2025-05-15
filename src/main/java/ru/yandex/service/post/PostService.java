package ru.yandex.service.post;

import ru.yandex.DTO.PostDTO;
import ru.yandex.model.Post;
import ru.yandex.paging.Paging;

import java.util.List;


public interface PostService {
    List<Post> findAll(int pageSize,int pageNumber);

    void save(PostDTO post);

    void deleteById(int id);

    Post findById(int id);

    void like(int id, boolean decision);

    void editPost(Post post);
    List<Post> findAllByTagOfDefault(String tag,int skip,int limit);

    Paging getPaging(String tag, int pageSize, int pageNumber);
}
