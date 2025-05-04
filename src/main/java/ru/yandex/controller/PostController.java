package ru.yandex.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.DTO.PostDTO;
import ru.yandex.mapper.PostMapper;
import ru.yandex.model.Post;
import ru.yandex.service.post.PostService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;


    @PostMapping(value = "/{id}")
    public String editPost(Model model, @PathVariable(name = "id") Integer id, @RequestParam(name = "post") PostDTO postDTO) {
        System.out.println(postDTO);
        model.addAttribute("post", postService.findById(id));
        return "post";
    }

    @GetMapping(value = "/{id}")
    public String getPost(Model model, @PathVariable(name = "id") Integer id) {
        model.addAttribute("post", postService.findById(id));
        return "post";
    }

    @PostMapping
    public String postPost(@RequestParam(name = "post") PostDTO postDTO) {
        postService.save(postMapper.mapToPost(postDTO));
        return "redirect:/posts";
    }

    @GetMapping
    public String posts(Model model, @RequestParam(name = "search",defaultValue = "") String search) {
        List<Post> posts;
        if (search.isEmpty()) {
            posts = postService.findAll();
        } else {
            posts = postService.findAllByTag(search.trim());
        }
        model.addAttribute("posts", posts);
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
        model.addAttribute("post", postService.findById(id));
        return "add-post";
    }


    @GetMapping
    @RequestMapping("/add")
    public String create() {
        return "add-post";
    }


}