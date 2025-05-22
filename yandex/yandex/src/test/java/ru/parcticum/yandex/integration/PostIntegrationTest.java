package ru.parcticum.yandex.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import ru.parcticum.yandex.DAO.comment.CommentRepository;
import ru.parcticum.yandex.DAO.post.PostRepository;
import ru.parcticum.yandex.model.Comment;
import ru.parcticum.yandex.model.Post;


import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
public class PostIntegrationTest {

    @Value("${spring.image.savePath}")
    String imageSavePath;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("drop table if exists posts;");
        jdbcTemplate.execute("drop table if exists comments;");
        jdbcTemplate.execute("create table if not exists posts(\n" +
                "                                    id bigserial primary key,\n" +
                "                                    title varchar(30) not null,\n" +
                "                                    text varchar(256) not null,\n" +
                "                                    imageUrl varchar(256) not null,\n" +
                "                                    likes integer,\n" +
                "                                    tags varchar(256) not null\n" +
                "    );");
        jdbcTemplate.execute("create table if not exists comments(\n" +
                "                                   id bigserial primary key,\n " +
                "                                   post_id bigint,\n" +
                "                                   text varchar(60) not null\n" +
                "    );");
        jdbcTemplate.execute("insert into comments(text,post_id) values('Good',1);\n" +
                "insert into comments(text,post_id) values('Bad',2);\n" +
                "insert into comments(text,post_id) values('Not bad',3);");
        jdbcTemplate.execute("insert into posts(title, text, imageUrl,likes,tags) values ('Post 1', 'Text 1', '" + imageSavePath + "1.jpg', 1,'sport');");
        jdbcTemplate.execute("insert into posts(title, text, imageUrl,likes,tags) values ('Post 2', 'Text 2', '" + imageSavePath + "1.jpg', 3,'sport');");
        jdbcTemplate.execute("insert into posts(title, text, imageUrl,likes,tags) values ('Post 3', 'Text 3', '" + imageSavePath + "1.jpg', 2,'sport');");
    }


    @Test
    void getPosts_shouldReturnHtmlWithPosts() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(xpath("//table//tr").nodeCount(4))
                .andExpect(xpath("//table//tr[2]//td//h2").string("Post 1"));
    }

    @Test
    void save_shouldAddPostToDatabaseAndRedirect() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", imageSavePath + "1.jpg", "image/jpeg", "test".getBytes());
        System.out.println(file.getOriginalFilename());
        mockMvc.perform(multipart("/posts")
                        .part(new MockPart("title", "Title".getBytes()))
                        .part(new MockPart("text", "Text".getBytes()))
                        .file(file)
                        .part(new MockPart("tags", "sport".getBytes())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
        assertThat(postRepository.findAll().size()).isEqualTo(4);
    }

    @Test
    void delete_shouldRemovePostFromDatabaseAndRedirect() throws Exception {
        mockMvc.perform(post("/posts/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
        assertThat(postRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    void edit_shouldChangeTitleInPost() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", new byte[0]);
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
        Post post=postRepository.findById(1).get();
        assertThat(post.getTitle()).isEqualTo("ChangedTitle");
    }

    @Test
    void addComment_shouldAddCommentAndReturnFreshStateOfPost() throws Exception {
        mockMvc.perform(post("/posts/{id}/comments", 1).param("text", "text"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(xpath("//table//tr").nodeCount(7))
                .andExpect(xpath("//table//tr[6]//td//form//span").string("text"));
        assertThat(commentRepository.findAllByPostId(1).size()).isEqualTo(2);
    }

    @Test
    void editComment_shouldEditCommentAndReturnFreshStateOfPost() throws Exception {
        mockMvc.perform(post("/posts/{id}/comments/{commentsId}", 1, 1).param("text", "ChangedText"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(xpath("//table//tr").nodeCount(6))
                .andExpect(xpath("//table//tr[5]//td//form//span").string("ChangedText"));
        List<Comment> comments = commentRepository.findAllByPostId(1);
        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.getFirst().getText()).isEqualTo("ChangedText");

    }

    @Test
    void deleteComment_shouldDeleteCommentAndReturnFreshStateOfPost() throws Exception {
        mockMvc.perform(post("/posts/{id}/comments/{commentsId}/delete", 1, 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(xpath("//table//tr").nodeCount(5));
        assertThat(commentRepository.findAllByPostId(1).size()).isEqualTo(0);

    }
}