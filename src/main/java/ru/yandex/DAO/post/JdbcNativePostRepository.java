package ru.yandex.DAO.post;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcNativePostRepository implements PostRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcNativePostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> getTagsByPostId(int postId) {
        List<String> tags = new ArrayList<>();
        jdbcTemplate.query("SELECT tags.tag FROM posts join posts_tags on posts.id = posts_tags.post_id join tags on tag_id=tags.id where posts.id=?",
                new Object[]{postId},
                (rs, rowNum) -> {
                    tags.add(rs.getString("tag"));
                    return tags;
                });
        return tags;
    }

    @Override
    public List<Post> findAll() {
        return jdbcTemplate.query(
                "select id, title, text, imageUrl,likes from posts",
                (rs, rowNum) -> new Post(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("text"),
                        rs.getString("imageUrl"),
                        rs.getInt("likes"),
                        getTagsByPostId(rs.getInt("id"))
                ));
    }

    @Override
    public void save(Post post) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update("insert into posts(title, text, imageUrl, likes) values(?, ?, ?, ?)",
                post.getTitle(), post.getText(), post.getImageUrl(), post.getLikes(), keyHolder);
        int insertedId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        post.getTags().forEach(tagName -> Optional.ofNullable(jdbcTemplate.queryForObject("select tags.id from tags where tag=?",
                Integer.class, tagName)).ifPresent(tagId -> jdbcTemplate.update("insert into posts_tags values(?, ?)", insertedId, tagId)));
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        jdbcTemplate.update("delete from posts where id = ?", id);
        jdbcTemplate.update("delete from posts_tags where post_id = ?", id);
    }

    public Optional<Post> findById(int id) {
        return Optional.ofNullable(jdbcTemplate.query("select id, title, text, imageUrl,likes from posts where id = ?", new Object[]{id},
                rs -> {
                    if (rs.next()) {
                        return new Post(rs.getInt("id"),
                                rs.getString("title"),
                                rs.getString("text"),
                                rs.getString("imageUrl"),
                                rs.getInt("likes"),
                                getTagsByPostId(rs.getInt("id"))
                        );
                    } else {
                        return null;
                    }
                }));
    }

    @Override
    public void updateLikesCount(int id, boolean decision) {
        if (decision) {
            jdbcTemplate.update("update posts set likes=NULLIF(likes, 0) + 1 where id=?", id);
        } else {
            jdbcTemplate.update("update posts set likes=NULLIF(likes, 1) - 1 where id=?", id);
        }
    }
}
