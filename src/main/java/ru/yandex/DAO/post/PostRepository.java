package ru.yandex.DAO.post;

import ru.yandex.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    List<Post> findAll();
    void save(Post post);
    void deleteById(int id);
    Optional<Post> findById(int id);
    void updateLikesCount(int id,boolean decision);
}
