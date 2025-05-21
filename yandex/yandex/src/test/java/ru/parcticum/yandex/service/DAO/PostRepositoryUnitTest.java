package ru.parcticum.yandex.service.DAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.parcticum.yandex.DAO.post.PostRepository;
import ru.parcticum.yandex.model.Post;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
public class PostRepositoryUnitTest {

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    public void setUp() {
        postRepository.deleteAll();
    }

    @Test
    public void testSavePost() {
        Post post = new Post(null, "title", "text1234567890", "imageUrl", 0, "sport");
        Post savedPost = postRepository.save(post);
        assertThat(savedPost.getText()).isEqualTo(post.getText());
        assertThat(savedPost.getImageUrl()).isEqualTo(post.getImageUrl());
    }

    @Test
    public void testDeletePost() {
        Post post = new Post(null, "title", "text1234567890", "imageUrl", 0, "sport");
        Post savedPost = postRepository.save(post);
        assertThat(postRepository.findAll().size()).isEqualTo(1);
        postRepository.deleteById(savedPost.getId());
        assertThat(postRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    public void testFindPost() {
        Post post = new Post(null, "title", "text1234567890", "imageUrl", 0, "sport");
        Post savedPost = postRepository.save(post);
        assertThat(postRepository.findAll().size()).isEqualTo(1);
        Post foundedPost = postRepository.findById(savedPost.getId()).get();
        assertThat(foundedPost).isNotEqualTo(null);
        assertThat(foundedPost.getText()).isEqualTo(post.getText());
    }


}
