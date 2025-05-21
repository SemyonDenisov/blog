package ru.parcticum.yandex.service.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.parcticum.yandex.DAO.post.PostRepository;
import ru.parcticum.yandex.DTO.PostDTO;
import ru.parcticum.yandex.DTO.PostWithCommentsDTO;
import ru.parcticum.yandex.model.Post;

import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertNotNull;


@SpringBootTest
public class PostServiceUnitTest {
    @MockitoBean
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @BeforeEach
    void resetMocks() {
        reset(postRepository);
    }

    @Test
    void testSavePost_success() {
        when(postRepository.save(any(Post.class))).thenReturn(new Post());
        postService.save(new PostDTO("title","text","imageUrl","sport"));
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void testFindPostById_success() {
        Post mockPost = new Post(1,
                "title",
                "text",
                "imageUrl",
                0,
                "sport, reading");
        doReturn(Optional.of(mockPost)).when(postRepository).findById(1);
        PostWithCommentsDTO post = new PostWithCommentsDTO(postRepository.findById(1).get(),new ArrayList<>());
        assertNotNull("Post must be not null", post);
        assertEquals("text must be equal text", mockPost.getText(), "text");
    }

    @Test
    void testEditPostById_success() {
        Post mockPost = new Post(1,
                "title",
                "text",
                "imageUrl",
                0,
                "sport, reading");
        doReturn(Optional.of(mockPost)).when(postRepository).findById(1);
        PostWithCommentsDTO post = postService.findById(1);
        assertNotNull("Post must be not null", post);
        assertEquals("text must be equal text", mockPost.getText(), "text");
    }

}