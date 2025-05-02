package ru.yandex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.model.Post;
import ru.yandex.service.post.PostService;

import java.util.List;
import java.util.Map;

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
    public String likePost(Model model,@PathVariable(name="id") Integer id,@RequestParam Map<String, Object>  like) {
        postService.like(id, Boolean.parseBoolean(like.get("like").toString()));
        model.addAttribute("post", postService.findById(id));
        return "post";
    }

    @GetMapping
    @RequestMapping("/{id}")
    public String getPost(Model model,@PathVariable(name = "id") Integer id) {
        model.addAttribute("post", postService.findById(id));
        return "post";
    }

    @PostMapping(value = "/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id) {
        postService.deleteById(id);
        return "redirect:/posts";
    }
    @GetMapping(value = "/{id}/edit")
    public String edit(Model model,@PathVariable(name = "id") Integer id) {
        model.addAttribute("post",postService.findById(id));
        return "add-post";
    }


    @GetMapping
    @RequestMapping("/add")
    public String save() {
        return "add-post";
    }


}