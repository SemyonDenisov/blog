package ru.yandex.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.DTO.PostDTO;
import ru.yandex.DTO.PostEditDTO;
import ru.yandex.mapper.PostMapper;
import ru.yandex.model.Post;
import ru.yandex.model.Tag;
import ru.yandex.paging.Paging;
import ru.yandex.service.post.PostService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;


    @PostMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String editPost(Model model,
                           @PathVariable(name = "id") Integer id,
                           @RequestPart(name = "title") String title,
                           @RequestPart(name = "text") String text,
                           @RequestPart(name = "image", required = false) MultipartFile image,
                           @RequestPart(name = "tags") String tags) {
        Post post = postService.findById(id);
        post.setTitle(title);
        post.setText(text);
        post.setTags(tags);
        postService.editPost(post);
        model.addAttribute("post", postService.findById(id));
        return "post";
    }

    @GetMapping(value = "/{id}")
    public String getPost(Model model, @PathVariable(name = "id") Integer id) {
        model.addAttribute("post", postService.findById(id));
        return "post";
    }

    @PostMapping
    public String postPost(@RequestPart(name = "title") String title,
                            @RequestPart(name = "text") String text,
                            @RequestPart(name = "image", required = false) MultipartFile image,
                            @RequestPart(name = "tags") String tags) {
        PostDTO postDTO = new PostDTO(title,text,"",tags);
        postService.save(postDTO);
        return "redirect:/posts";
    }

    @GetMapping
    public String posts(Model model,
                        @RequestParam(name = "search", defaultValue = "") String search,
                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                        @RequestParam(name = "pageNumber", defaultValue = "1") Integer pageNumber
    ) {
        List<Post> posts = postService.findAllByTagOfDefault(search.trim(), pageSize, pageNumber);
        Paging paging = postService.getPaging(search.trim(), pageSize, pageNumber);
        model.addAttribute("posts", posts);
        model.addAttribute("paging", paging);
        return "posts";
    }

    @PostMapping
    @RequestMapping("/{id}/like")
    public String likePost(Model model, @PathVariable(name = "id") Integer id, @RequestParam Map<String, Object> like) {
        postService.like(id, Boolean.parseBoolean(like.get("like").toString()));
        model.addAttribute("post", postService.findById(id));
        return "post";
    }


    @PostMapping(value = "{id}/comments/{commentId}")
    public String editComment(@PathVariable(name = "id") Integer id,
                              @PathVariable(name = "commentId") Integer commentId,
                              @RequestParam(name = "text") String text, Model model) {
        postService.editComment(commentId, text);
        model.addAttribute("post", postService.findById(id));
        return "post";
    }

    @PostMapping(value = "{id}/comments")
    public String createComment(@PathVariable(name = "id") Integer id,
                                @RequestParam(name = "text") String text, Model model) {
        postService.createComment(id, text);
        model.addAttribute("post", postService.findById(id));
        return "post";
    }

    @PostMapping(value = "{id}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable(name = "id") Integer id, @PathVariable(name = "commentId") Integer commentId, Model model) {
        postService.deleteComment(commentId);
        model.addAttribute("post", postService.findById(id));
        return "post";
    }


    @PostMapping(value = "/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id) {
        postService.deleteById(id);
        return "redirect:/posts";
    }

    @GetMapping(value = "/{id}/edit")
    public String edit(Model model, @PathVariable(name = "id") Integer id) {
        Post p = postService.findById(id);
        model.addAttribute("post", p);
        return "add-post";
    }


    @GetMapping
    @RequestMapping("/add")
    public String create() {
        return "add-post";
    }


}