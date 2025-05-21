package ru.parcticum.yandex.service.post;


import org.springframework.stereotype.Service;
import ru.parcticum.yandex.DAO.post.PostRepository;
import ru.parcticum.yandex.DTO.PostDTO;
import ru.parcticum.yandex.DTO.PostWithCommentsDTO;
import ru.parcticum.yandex.mapper.PostMapper;
import ru.parcticum.yandex.model.Post;
import ru.parcticum.yandex.service.comment.CommentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentService commentService;
    private final PostMapper postMapper;

    public PostServiceImpl(PostRepository postRepository, CommentService commentService,PostMapper postMapper) {
        this.postRepository = postRepository;
        this.commentService = commentService;
        this.postMapper = postMapper;
    }

    @Override
    public List<PostWithCommentsDTO> findAll(int pageSize, int pageNumber) {
        List<PostWithCommentsDTO> postWithCommentsDTOS = new ArrayList<>();
        List<Post> posts = postRepository.findAll();

        posts.forEach(post -> {
            postWithCommentsDTOS.add(new PostWithCommentsDTO(post, commentService.findComments(post.getId())));
        });
        return postWithCommentsDTOS;
    }

    @Override
    public void save(PostDTO post) {
        postRepository.save(new Post(null, post.title(), post.text(), post.imageUrl(), 0, ""));
    }

    @Override
    public void deleteById(int id) {
        postRepository.deleteById(id);
    }

    @Override
    public PostWithCommentsDTO findById(int id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            return new PostWithCommentsDTO(post.get(), commentService.findComments(id));
        } else {
            throw new RuntimeException("Post with id: " + id + " not found");
        }
    }

    @Override
    public void editPost(Post post) {
        postRepository.save(post);
    }


    @Override
    public void like(int id, boolean decision) {
        postRepository.findById(id).ifPresentOrElse(
                post -> {
                    if (decision) {
                        postRepository.updateLikesCount(id, true);
                    } else if (post.getLikes() > 0) {
                        postRepository.updateLikesCount(id, false);
                    }
                },
                () -> {
                }
        );
    }

    @Override
    public List<PostWithCommentsDTO> findAllByTagOfDefault(String tag, int pageSize, int pageNumber) {
        tag = tag.trim();
        List<Post> posts;
        if (tag.isEmpty()) {
            posts = postRepository.findAll();
        } else {
            posts = postRepository.findAll();///by tag
        }
        List<PostWithCommentsDTO> postWithCommentsDTOS = new ArrayList<>();
        posts.forEach(post -> {
            postWithCommentsDTOS.add(new PostWithCommentsDTO(post, commentService.findComments(post.getId())));
        });
        return postWithCommentsDTOS;
        //return postRepository.findAllByTagOfDefault(tag,pageSize,pageNumber);
    }
//    @Override
//    public Paging getPaging(String tag, int pageSize, int pageNumber){
//        return postRepository.getPaging(tag,pageSize,pageNumber);
//    }
}
