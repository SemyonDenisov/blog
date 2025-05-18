package ru.yandex.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.DAO.post.PostRepository;
import ru.yandex.DTO.PostDTO;
import ru.yandex.configuration.TestConfig;
import ru.yandex.model.Post;
import ru.yandex.service.post.PostService;

import java.util.ArrayList;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public class PostServiceUnitTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @BeforeEach
    void resetMocks() {
        reset(postRepository);
    }

    @Test
    void testSavePost_success() {
        PostDTO post = new PostDTO(
                "test",
                "text",
                "imageUrl",
                "sport, reading"
        );
        doNothing().when(postRepository).save(post);
        postService.save(post);
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testFindPostById_success() {
        Post mockPost = new Post(1,
                "title",
                "text",
                "imageUrl",
                0,
                "sport, reading",
                new ArrayList<>());
        doReturn(Optional.of(mockPost)).when(postRepository).findById(1);
        Post post = postService.findById(1);
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
                "sport, reading",
                new ArrayList<>());
        doReturn(Optional.of(mockPost)).when(postRepository).findById(1);
        Post post = postService.findById(1);
        assertNotNull("Post must be not null", post);
        assertEquals("text must be equal text", mockPost.getText(), "text");
    }

}