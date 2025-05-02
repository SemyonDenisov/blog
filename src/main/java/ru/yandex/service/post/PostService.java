package ru.yandex.service.post;

import org.springframework.stereotype.Service;
import ru.yandex.model.Post;

import java.util.List;



public interface PostService {
    List<Post> findAll();
    void save(Post post);
    void deleteById(int id);
    Post findById(int id);
    void like(int id,boolean decision);
}
