package ru.parcticum.yandex.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.parcticum.yandex.DTO.PostDTO;
import ru.parcticum.yandex.DTO.PostWithCommentsDTO;
import ru.parcticum.yandex.model.Comment;
import ru.parcticum.yandex.model.Post;
import ru.parcticum.yandex.paging.Paging;
import ru.parcticum.yandex.service.comment.CommentServiceImpl;
import ru.parcticum.yandex.service.post.PostServiceImpl;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PostController.class)
@TestPropertySource(locations = "classpath:application-test.yaml")
public class PostControllerUnitTest {

    @Value("${spring.image.savePath}")
    String imageSavePath;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostServiceImpl postService;

    @MockitoBean
    private CommentServiceImpl commentService;


    @BeforeEach
    void setUp() {
        reset(postService);
    }


    @Test
    void getPosts_shouldReturnHtmlWithPosts() throws Exception {
        List<PostWithCommentsDTO> posts = new ArrayList<>();
        posts.add(new PostWithCommentsDTO(new Post(1, "title1", "text1", "imageUrl1", 0, "sport"), new ArrayList<>()));
        posts.add(new PostWithCommentsDTO(new Post(2, "title2", "text2", "imageUrl2", 2, "sport"), new ArrayList<>()));
        posts.add(new PostWithCommentsDTO(new Post(3, "title3", "text3", "imageUrl3", 3, "sport"), new ArrayList<>()));

        when(postService.findAllByTagOfDefault("",10, 1))
                .thenReturn(posts);
        when(postService.getPaging("",10,1))
                .thenReturn(new Paging(10,1,false,false));
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(xpath("//table//tr").nodeCount(4))
                .andExpect(xpath("//table//tr[2]//td//h2").string("title1"));
    }

    @Test
    void save_shouldRedirect() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", imageSavePath + "1.jpg", "image/jpeg", "test".getBytes());
        System.out.println(file.getOriginalFilename());
        doNothing().when(postService).save(new PostDTO("title", "text", file.getOriginalFilename(), "sport"));
        mockMvc.perform(multipart("/posts")
                        .part(new MockPart("title", "Title".getBytes()))
                        .part(new MockPart("text", "Text".getBytes()))
                        .file(file)
                        .part(new MockPart("tags", "sport".getBytes())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    void delete_shouldRedirect() throws Exception {
        doNothing().when(postService).deleteById(1);
        mockMvc.perform(post("/posts/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    void edit_shouldChangeTitleInPost() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", new byte[0]);
        Post post = new Post(1, "ChangedTitle", "Text", file.getOriginalFilename(), 0, "sport");
        PostWithCommentsDTO postWithComments = new PostWithCommentsDTO(
                post,
                new ArrayList<>());
        doNothing().when(postService).editPost(post);
        when(postService.findById(1)).thenReturn(postWithComments);
        mockMvc.perform(
                        multipart("/posts/{id}", 1)
                                .part(new MockPart("title", "ChangedTitle".getBytes()))
                                .part(new MockPart("text", "Text".getBytes()))
                                .part(new MockPart("tags", "sport".getBytes()))
                                .file(file)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//table//tr[2]//td//h2").string("ChangedTitle"));
        ;
    }

    @Test
    void addComment_shouldAddCommentAndReturnFreshStateOfPost() throws Exception {
        doNothing().when(commentService).createComment(1, "text");
        when(postService.findById(1)).thenReturn(new PostWithCommentsDTO(1,
                "title",
                "text",
                "imageUrl",
                0,
                "sport",
                List.of(new Comment(1, 1, "text"))));
        mockMvc.perform(post("/posts/{id}/comments", 1).param("text", "text"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(xpath("//table//tr").nodeCount(6))
                .andExpect(xpath("//table//tr[5]//td//form//span").string("text"));
    }

    @Test
    void editComment_shouldEditCommentAndReturnFreshStateOfPost() throws Exception {
        doNothing().when(commentService).editComment(1, 1, "text");
        when(postService.findById(1)).thenReturn(new PostWithCommentsDTO(1,
                "title",
                "text",
                "imageUrl",
                0,
                "sport",
                List.of(new Comment(1, 1, "text"))));
        when(commentService.findComments(1)).thenReturn(List.of(new Comment(1, 1, "text")));
        mockMvc.perform(post("/posts/{id}/comments/{commentsId}", 1, 1).param("text", "text"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(xpath("//table//tr").nodeCount(6))
                .andExpect(xpath("//table//tr[5]//td//form//span").string("text"));
    }

    @Test
    void deleteComment_shouldDeleteCommentAndReturnFreshStateOfPost() throws Exception {
        doNothing().when(commentService).deleteComment(1);
        when(postService.findById(1)).thenReturn(new PostWithCommentsDTO(1,
                "title",
                "text",
                "imageUrl",
                0,
                "sport",
                List.of(new Comment(1, 1, "text"))));
        mockMvc.perform(post("/posts/{id}/comments/{commentsId}/delete", 1, 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(xpath("//table//tr").nodeCount(6));
    }
}