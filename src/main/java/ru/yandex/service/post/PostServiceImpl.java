package ru.yandex.service.post;


import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import ru.yandex.DAO.post.PostRepository;
import ru.yandex.model.Post;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public void save(Post post) {
        postRepository.save(post);
    }

    @Override
    public void deleteById(int id) {
        postRepository.deleteById(id);
    }

    @Override
    public Post findById(int id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public void edit(Post post) {

    }

    @Override
    public void like(int id, boolean decision) {
        postRepository.findById(id).ifPresentOrElse(
                post -> {
                    if (decision) {
                        postRepository.updateLikesCount(id,true);
                    } else {
                        postRepository.updateLikesCount(id,false);

                    }
                },
                () -> {
                    return;
                }
        );
    }
}
