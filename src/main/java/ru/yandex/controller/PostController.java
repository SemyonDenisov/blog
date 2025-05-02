package ru.yandex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.model.Post;
import ru.yandex.service.post.PostService;

import javax.swing.text.Document;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public String posts(Model model) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "posts";
    }

    @PostMapping
    @RequestMapping("/{id}/like")
    public String likePost(@PathVariable(name="id") Integer id,@RequestParam Boolean like, Model model) {
        postService.like(id, like);
        model.addAttribute("post", postService.findById(id));
        return "post";
    }

    @GetMapping
    @RequestMapping("/{id}")
    public String getPost(@PathVariable(name = "id") Integer id, Model model) {
        model.addAttribute("post", postService.findById(id));
        return "post";
    }

    @PostMapping(value = "/{id}", params = "_method=delete")
    public String delete(@PathVariable(name = "id") Integer id) {
        postService.deleteById(id);
        return "redirect:/posts";
    }

    @GetMapping
    @RequestMapping("/add")
    public String save(@ModelAttribute Post post) {
        return "add-post";
    }


}