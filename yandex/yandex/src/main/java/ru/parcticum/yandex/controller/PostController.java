package ru.parcticum.yandex.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.parcticum.yandex.DTO.PostDTO;
import ru.parcticum.yandex.DTO.PostWithCommentsDTO;
import ru.parcticum.yandex.model.Post;
import ru.parcticum.yandex.paging.Paging;
import ru.parcticum.yandex.service.comment.CommentService;
import ru.parcticum.yandex.service.post.PostService;
import ru.parcticum.yandex.tools.InputValidator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    @Value("${spring.image.savePath}")
    private String imageSavePath;

    @PostMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String editPost(Model model,
                           @PathVariable(name = "id") Integer id,
                           @RequestPart(name = "title") String title,
                           @RequestPart(name = "text") String text,
                           @RequestPart(name = "image", required = false) MultipartFile image,
                           @RequestPart(name = "tags") String tags) {
        Post post = new Post(postService.findById(id));
        post.setTitle(title);
        post.setText(text);
        post.setTags(tags);
        if (image != null) {
            try {
                if (InputValidator.isValidImage(image)) {
                    UUID uuid = UUID.randomUUID();
                    String extension = Objects.requireNonNull(image.getOriginalFilename()).split("\\.")[1];
                    String newImageUrl = imageSavePath + "\\" + uuid + "." + extension;
                    image.transferTo(new File(newImageUrl));
                    if (!new File(post.getImageUrl()).delete()) {
                        System.out.println("Cannot delete file: " + post.getImageUrl());
                    }
                    post.setImageUrl(newImageUrl);
                }
            } catch (Exception ignored) {
            }
        }
        postService.editPost(post);
        model.addAttribute("post", postService.findById(id));
        return "post";
    }

    @GetMapping(value = "/{id}")
    public String getPost(Model model, @PathVariable(name = "id") Integer id) {
        model.addAttribute("post", postService.findById(id));
        return "post";
    }

    @GetMapping(value = "/images/{id}")
    public @ResponseBody byte[] getImage(@PathVariable(name = "id") Integer id) {
        try {
            Post post = new Post(postService.findById(id));
            return Files.readAllBytes(Path.of(post.getImageUrl()));
        } catch (Exception ignored) {
        }
        return new byte[0];
    }

    @PostMapping
    public String postPost(@RequestPart(name = "title") String title,
                           @RequestPart(name = "text") String text,
                           @RequestPart(name = "image") MultipartFile image,
                           @RequestPart(name = "tags") String tags) throws IOException {
        if (InputValidator.isValidImage(image)) {
            UUID uuid = UUID.randomUUID();
            String extension = Objects.requireNonNull(image.getOriginalFilename()).split("\\.")[1];
            String imageUrl = imageSavePath + "\\" + uuid + "." + extension;
            image.transferTo(new File(imageUrl));
            PostDTO postDTO = new PostDTO(title, text, imageUrl, tags);
            postService.save(postDTO);
        } else {
            System.out.println("Invalid image");
        }
        return "redirect:/posts";
    }

    @GetMapping
    public String posts(Model model,
                        @RequestParam(name = "search", defaultValue = "") String search,
                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                        @RequestParam(name = "pageNumber", defaultValue = "1") Integer pageNumber
    ) {
        search = search.trim();
        List<PostWithCommentsDTO> posts = postService.findAllByTagOfDefault(search.trim(), pageSize, pageNumber);
        Paging paging = postService.getPaging(search, pageSize, pageNumber);
        model.addAttribute("posts", posts);
        model.addAttribute("search", search);
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
        commentService.editComment(commentId, id, text);
        model.addAttribute("post", postService.findById(id));
        return "post";
    }

    @PostMapping(value = "{id}/comments")
    public String createComment(@PathVariable(name = "id") Integer id,
                                @RequestParam(name = "text") String text, Model model) {
        commentService.createComment(id, text);
        model.addAttribute("post", postService.findById(id));
        return "post";
    }

    @PostMapping(value = "{id}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable(name = "id") Integer id, @PathVariable(name = "commentId") Integer commentId, Model model) {
        commentService.deleteComment(commentId);
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
        PostWithCommentsDTO p = postService.findById(id);
        model.addAttribute("post", p);
        return "add-post";
    }

    @GetMapping
    @RequestMapping("/add")
    public String create() {
        return "add-post";
    }
}