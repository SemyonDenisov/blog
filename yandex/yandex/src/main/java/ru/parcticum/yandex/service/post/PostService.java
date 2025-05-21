package ru.parcticum.yandex.service.post;

import ru.parcticum.yandex.DTO.PostDTO;
import ru.parcticum.yandex.DTO.PostWithCommentsDTO;
import ru.parcticum.yandex.model.Post;
import ru.parcticum.yandex.paging.Paging;

import java.util.List;


public interface PostService {
    List<PostWithCommentsDTO> findAll(int pageSize, int pageNumber);

    void save(PostDTO post);

    void deleteById(int id);

    PostWithCommentsDTO findById(int id);

    void like(int id, boolean decision);

    void editPost(Post post);
    List<PostWithCommentsDTO> findAllByTagOfDefault(String tag, int skip, int limit);

    Paging getPaging(String tag, int pageSize, int pageNumber);
}
