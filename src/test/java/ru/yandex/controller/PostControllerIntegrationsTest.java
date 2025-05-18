package ru.yandex.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.configuration.TestDataSourceConfiguration;
import ru.yandex.configuration.TestConfig;
import ru.yandex.configuration.WebConfiguration;
import ru.yandex.model.Post;


import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig(classes = {WebConfiguration.class, ru.yandex.configuration.integration.TestConfig.class, TestDataSourceConfiguration.class})
@TestPropertySource(locations = "classpath:application-test.properties")
public class PostControllerIntegrationsTest {

    @Value("${spring.image.savePath}")
    String imageSavePath;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        jdbcTemplate.execute("drop table if exists posts;");
        jdbcTemplate.execute("create table if not exists posts(\n" +
                "                                    id bigserial primary key,\n" +
                "                                    title varchar(30) not null,\n" +
                "                                    text varchar(256) not null,\n" +
                "                                    imageUrl varchar(256) not null,\n" +
                "                                    likes integer,\n" +
                "                                    tags varchar(256) not null\n" +
                "    );");
        jdbcTemplate.execute("insert into posts(title, text, imageUrl,likes,tags) values ('Post 1', 'Text 1', '" + imageSavePath + "1.jpg', 1,'sport');");
        jdbcTemplate.execute("insert into posts(title, text, imageUrl,likes,tags) values ('Post 2', 'Text 2', '" + imageSavePath + "2.jpg', 3,'sport');");
        List<Post> posts = jdbcTemplate.query("select * from posts;", (rs, rowNum) -> new Post(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("text"),
                rs.getString("imageUrl"),
                rs.getInt("likes"),
                rs.getString("tags"),
                new ArrayList<>())
        );
        posts.forEach(post -> {System.out.println(post.getId());});
    }


    @Test
    void getPosts_shouldReturnHtmlWithPosts() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(xpath("//table//tr").nodeCount(3))
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
    }

    @Test
    void delete_shouldRemovePostFromDatabaseAndRedirect() throws Exception {
        mockMvc.perform(post("/posts/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    void edit_shouldChangeTitleInPost() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", new byte[0]);
        mockMvc.perform(
                        multipart("/posts/{id}",1)
                                .part(new MockPart("title", "ChangedTitle".getBytes()))
                                .part(new MockPart("text", "Text".getBytes()))
                                .part(new MockPart("tags", "sport".getBytes()))
                                .file(file)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//table//tr[2]//td//h2").string("ChangedTitle"));;
    }
}