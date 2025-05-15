package ru.yandex.service.post;


import org.springframework.stereotype.Service;
import ru.yandex.DAO.post.PostRepository;
import ru.yandex.DTO.PostDTO;
import ru.yandex.model.Post;
import ru.yandex.paging.Paging;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> findAll(int pageSize,int pageNumber) {
        return postRepository.findAll(pageSize,pageNumber);
    }

    @Override
    public void save(PostDTO post) {
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
    public void editPost(Post post) {
        postRepository.updatePost(post);
    }


    @Override
    public void like(int id, boolean decision) {
        postRepository.findById(id).ifPresentOrElse(
                post -> {
                    if (decision) {
                        postRepository.updateLikesCount(id,true);
                    } else if(post.getLikes()>0) {
                        postRepository.updateLikesCount(id,false);
                    }
                },
                () -> {
                }
        );
    }

    @Override
    public List<Post> findAllByTagOfDefault(String tag,int pageSize,int pageNumber) {
        return postRepository.findAllByTagOfDefault(tag,pageSize,pageNumber);
    }
    @Override
    public Paging getPaging(String tag, int pageSize, int pageNumber){
        return postRepository.getPaging(tag,pageSize,pageNumber);
    }
}
