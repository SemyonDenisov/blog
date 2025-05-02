package ru.yandex.DAO.post;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.model.Post;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcNativePostRepository implements PostRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcNativePostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
                        rs.getInt("likes")
                ));
    }

    @Override
    public void save(Post post) {
        jdbcTemplate.update("insert into posts(title, text, imageUrl, likes) values(?, ?, ?, ?)",
                post.getTitle(), post.getText(), post.getImageUrl(), post.getLikes());
    }

    @Override
    public void deleteById(int id) {
        jdbcTemplate.update("delete from posts where id = ?", id);
    }

    public Optional<Post> findById(int id) {
        return Optional.ofNullable(jdbcTemplate.query("select id, title, text, imageUrl,likes from posts where id = ?", new Object[]{id},
                rs -> {
                    if (rs.next()) {
                        return new Post(rs.getInt("id"),
                                rs.getString("title"),
                                rs.getString("text"),
                                rs.getString("imageUrl"),
                                rs.getInt("likes"));
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
