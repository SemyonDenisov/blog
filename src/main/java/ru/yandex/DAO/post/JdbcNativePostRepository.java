package ru.yandex.DAO.post;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.DTO.PostDTO;
import ru.yandex.model.Comment;
import ru.yandex.model.Post;
import ru.yandex.paging.Paging;

import java.sql.Statement;
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

    public List<Comment> getCommentsByPostId(int postId) {
        return new ArrayList<>(jdbcTemplate.query("SELECT comments.id,comments.text FROM posts join posts_comments on posts.id = posts_comments.post_id join comments on comment_id=comments.id where posts.id=?",
                new Object[]{postId},
                (rs, rowNum) -> new Comment(
                        rs.getInt("id"),
                        rs.getString("text"))));
    }

    @Override
    public List<Post> findAll(int pageSize, int pageNumber) {
        return jdbcTemplate.query(
                "select id, title, text, imageUrl,likes,tags from posts",
                (rs, rowNum) -> new Post(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("text"),
                        rs.getString("imageUrl"),
                        rs.getInt("likes"),
                        rs.getString("tags"),
                        getCommentsByPostId(rs.getInt("id"))
                ));
    }

    @Override
    @Transactional
    public void updatePost(Post post) {
        jdbcTemplate.update("update posts set title=?, text=?, imageUrl=?, likes=?,tags=? where id=?",
                post.getTitle(), post.getText(), post.getImageUrl(), post.getLikes(), post.getTagsAsText(), post.getId());

    }

    @Override
    public void save(PostDTO post) {
        jdbcTemplate.update("insert into posts(title, text, imageUrl, likes,tags) values(?, ?, ?, ?, ?)",
                post.title(), post.text(), post.imageUrl(), 0, post.tags());
    }

    @Override
    public void deleteById(int id) {
        jdbcTemplate.update("delete from posts where id = ?", id);
    }

    public Optional<Post> findById(int id) {
        return Optional.ofNullable(jdbcTemplate.query("select id, title, text, imageUrl,likes,tags from posts where id = ?", new Object[]{id},
                rs -> {
                    if (rs.next()) {
                        return new Post(rs.getInt("id"),
                                rs.getString("title"),
                                rs.getString("text"),
                                rs.getString("imageUrl"),
                                rs.getInt("likes"),
                                rs.getString("tags"),
                                getCommentsByPostId(rs.getInt("id"))
                        );
                    } else {
                        return null;
                    }
                }));
    }

    @Override
    public void updateLikesCount(int id, boolean decision) {
        if (decision) {
            jdbcTemplate.update("update posts set likes= likes + 1 where id=?", id);
        } else {
            jdbcTemplate.update("update posts set likes=likes - 1 where id=?", id);
        }
    }




    @Override
    public List<Post> findAllByTagOfDefault(String tag, int pageSize, int pageNumber) {
        if (tag.isEmpty()) {
            return jdbcTemplate.query(
                    "select id, title, text, imageUrl,likes,tags from posts limit ? offset ?",
                    new Object[]{pageSize, (pageNumber - 1) * pageSize},
                    (rs, rowNum) -> new Post(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("text"),
                            rs.getString("imageUrl"),
                            rs.getInt("likes"),
                            rs.getString("tags"),
                            getCommentsByPostId(rs.getInt("id"))
                    ));
        }
        return jdbcTemplate.query("select posts.id, title, text, imageUrl,likes,tags from posts where tags like ? limit ? offset ?",
                new Object[]{"%" + tag + "%", pageSize, (pageNumber - 1) * pageSize},
                (rs, rowNum) -> new Post(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("text"),
                        rs.getString("imageUrl"),
                        rs.getInt("likes"),
                        rs.getString("tags"),
                        getCommentsByPostId(rs.getInt("id"))
                ));
    }

    @Override
    public Paging getPaging(String tag, int pageSize, int pageNumber) {
        if (tag.isEmpty()) {
            return jdbcTemplate.query("select count(*) as cnt from posts",
                    rs -> {
                        if (rs.next()) {
                            int count = rs.getInt(1);
                            boolean hasNext = count > pageSize * pageNumber;
                            boolean hasPrevious = pageNumber != 1;
                            return new Paging(pageSize, pageNumber, hasNext, hasPrevious);
                        } else {
                            return null;
                        }
                    }
            );

        }
        return jdbcTemplate.query("select count(*) as cnt from posts where tags like ?",
                new Object[]{"%" + tag + "%"},
                rs -> {
                    if (rs.next()) {
                        int count = rs.getInt("cnt");
                        boolean hasNext = count > pageSize * pageNumber;
                        boolean hasPrevious = pageNumber != 1;
                        return new Paging(pageSize, pageNumber, hasNext, hasPrevious);
                    } else {
                        return null;
                    }
                }
        );
    }

}
