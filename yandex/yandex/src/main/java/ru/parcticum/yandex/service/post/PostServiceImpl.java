package ru.parcticum.yandex.service.post;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.parcticum.yandex.DAO.post.PostRepository;
import ru.parcticum.yandex.DTO.PostDTO;
import ru.parcticum.yandex.DTO.PostWithCommentsDTO;
import ru.parcticum.yandex.model.Post;
import ru.parcticum.yandex.paging.Paging;
import ru.parcticum.yandex.service.comment.CommentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentService commentService;

    public PostServiceImpl(PostRepository postRepository, CommentService commentService) {
        this.postRepository = postRepository;
        this.commentService = commentService;
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
        postRepository.save(new Post(null, post.title(), post.text(), post.imageUrl(), 0, post.tags()));
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
                        post.setLikes(post.getLikes() + 1);
                    } else if (post.getLikes() > 0) {
                        post.setLikes(post.getLikes() - 1);
                    }
                    postRepository.save(post);
                },
                () -> {
                    throw new RuntimeException("Not found post with id: " + id);
                }
        );
    }

    @Override
    public List<PostWithCommentsDTO> findAllByTagOfDefault(String tag, int pageSize, int pageNumber) {
        tag = tag.trim();
        Page<Post> posts;
        if (tag.isEmpty()) {
            posts = postRepository.findAll(PageRequest.of(pageNumber-1, pageSize));
        } else {
            posts = postRepository.findAllByTagsContaining(tag, PageRequest.of(pageNumber - 1, pageSize));
        }
        List<PostWithCommentsDTO> postWithCommentsDTOS = new ArrayList<>();
        posts.forEach(post -> {
            postWithCommentsDTOS.add(new PostWithCommentsDTO(post, commentService.findComments(post.getId())));
        });
        return postWithCommentsDTOS;
        //return postRepository.findAllByTagOfDefault(tag,pageSize,pageNumber);
    }


    @Override
    public Paging getPaging(String tag, int pageSize, int pageNumber) {
        if (tag.trim().isEmpty()) {
            return new Paging(
                    pageSize,
                    pageNumber,
                    !postRepository.findAll(PageRequest.of(pageNumber, pageSize)).isEmpty(),
                    pageNumber!=1
            );
        }
        return new Paging(
                pageSize,
                pageNumber,
                !postRepository.findAllByTagsContaining(tag, PageRequest.of(pageNumber, pageSize)).isEmpty(),
                pageNumber!=1
        );
    }
}