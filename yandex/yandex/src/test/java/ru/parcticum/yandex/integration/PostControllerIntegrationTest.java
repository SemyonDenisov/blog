//package ru.parcticum.yandex.integration;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.mock.web.MockPart;
//import ru.parcticum.yandex.DTO.PostDTO;
//import ru.parcticum.yandex.DTO.PostWithCommentsDTO;
//import ru.parcticum.yandex.model.Post;
//import ru.parcticum.yandex.service.post.PostServiceImpl;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.mockito.Mockito.doNothing;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;
//
//@SpringBootTest
//public class PostControllerIntegrationTest {
//
//    @Value("${spring.image.savePath}")
//    String imageSavePath;
//    private PostServiceImpl postServiceImpl;
//
//
//    @BeforeEach
//    void setUp() {
//        postServiceImpl.save(new PostDTO("title","text","imageUrl","tags"));
//
//    }
//
//
//    @Test
//    void getPosts_shouldReturnHtmlWithPosts() throws Exception {
//        List<PostWithCommentsDTO> posts = new ArrayList<>();
//        posts.add(new PostWithCommentsDTO(new Post(1, "title1", "text1", "imageUrl1", 0, "sport"), new ArrayList<>()));
//        posts.add(new PostWithCommentsDTO(new Post(2, "title2", "text2", "imageUrl2", 2, "sport"), new ArrayList<>()));
//        posts.add(new PostWithCommentsDTO(new Post(3, "title3", "text3", "imageUrl3", 3, "sport"), new ArrayList<>()));
//
//        when(postService.findAll(1, 10))
//                .thenReturn(posts);
//        mockMvc.perform(get("/posts"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("text/html;charset=UTF-8"))
//                .andExpect(view().name("posts"))
//                .andExpect(model().attributeExists("posts"))
//                .andExpect(xpath("//table//tr").nodeCount(3))
//                .andExpect(xpath("//table//tr[2]//td//h2").string("title1"));
//    }
//
//    @Test
//    void save_shouldAddPostToDatabaseAndRedirect() throws Exception {
//        MockMultipartFile file = new MockMultipartFile("image", imageSavePath + "1.jpg", "image/jpeg", "test".getBytes());
//        System.out.println(file.getOriginalFilename());
//        doNothing().when(postService).save(new PostDTO("title", "text", file.getOriginalFilename(), "sport"));
//        mockMvc.perform(multipart("/posts")
//                        .part(new MockPart("title", "Title".getBytes()))
//                        .part(new MockPart("text", "Text".getBytes()))
//                        .file(file)
//                        .part(new MockPart("tags", "sport".getBytes())))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/posts"));
//    }
//
//    @Test
//    void delete_shouldRemovePostFromDatabaseAndRedirect() throws Exception {
//        doNothing().when(postService).deleteById(1);
//        mockMvc.perform(post("/posts/1/delete"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/posts"));
//    }
//
//    @Test
//    void edit_shouldChangeTitleInPost() throws Exception {
//        MockMultipartFile file = new MockMultipartFile("image", new byte[0]);
//        doNothing().when(postService).editPost(new Post(1, "ChangedTitle", "Text", file.getOriginalFilename(), 0, "sport"));
//        mockMvc.perform(
//                        multipart("/posts/{id}", 1)
//                                .part(new MockPart("title", "ChangedTitle".getBytes()))
//                                .part(new MockPart("text", "Text".getBytes()))
//                                .part(new MockPart("tags", "sport".getBytes()))
//                                .file(file)
//                )
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("text/html;charset=UTF-8"))
//                .andExpect(xpath("//table//tr[2]//td//h2").string("ChangedTitle"));
//        ;
//    }
//
//    @Test
//    void addComment_shouldAddCommentAndReturnFreshStateOfPost() throws Exception {
//        mockMvc.perform(post("/posts/{id}/comments", 1).param("text", "text"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("text/html;charset=UTF-8"))
//                .andExpect(view().name("post"))
//                .andExpect(model().attributeExists("post"))
//                .andExpect(xpath("//table//tr").nodeCount(16))
//                .andExpect(xpath("//table//tr[15]//td//form//span").string("text"));
//    }
//
//    @Test
//    void editComment_shouldEditCommentAndReturnFreshStateOfPost() throws Exception {
//        mockMvc.perform(post("/posts/{id}/comments/{commentsId}", 1, 1).param("text", "text"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("text/html;charset=UTF-8"))
//                .andExpect(view().name("post"))
//                .andExpect(model().attributeExists("post"))
//                .andExpect(xpath("//table//tr").nodeCount(11))
//                .andExpect(xpath("//table//tr[9]//td//form//span").string("text"));
//    }
//
//    @Test
//    void deleteComment_shouldDeleteCommentAndReturnFreshStateOfPost() throws Exception {
//        mockMvc.perform(post("/posts/{id}/comments/{commentsId}/delete", 1, 1))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("text/html;charset=UTF-8"))
//                .andExpect(view().name("post"))
//                .andExpect(model().attributeExists("post"))
//                .andExpect(xpath("//table//tr").nodeCount(9))
//                .andExpect(xpath("//table//tr[8]//td//form//span").string("Bad"));
//    }
//}
//
