package ru.yandex.DAO.post;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.model.Comment;
import ru.yandex.model.Post;
import ru.yandex.model.Tag;
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

    public List<Tag> getTagsByPostId(int postId) {
        return jdbcTemplate.query("SELECT tags.id,tags.tag FROM posts join posts_tags on posts.id = posts_tags.post_id join tags on tag_id=tags.id where posts.id=?",
                new Object[]{postId},
                (rs, rowNum) -> new Tag(rs.getInt("id"), rs.getString("tag")));
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
                "select id, title, text, imageUrl,likes from posts",
                (rs, rowNum) -> new Post(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("text"),
                        rs.getString("imageUrl"),
                        rs.getInt("likes"),
                        getTagsByPostId(rs.getInt("id")),
                        getCommentsByPostId(rs.getInt("id"))
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
                                getTagsByPostId(rs.getInt("id")),
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
    public void editComment(int commentId, String text) {
        jdbcTemplate.update("update comments set text= ? where comments.id=?", text, commentId);
    }

    @Override
    @Transactional
    public void createComment(int postId, String text) {
        final String SQL = "insert into comments(text) values('" + text + "')";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> connection.prepareStatement(SQL,
                Statement.RETURN_GENERATED_KEYS), keyHolder);
        int insertedId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        jdbcTemplate.update("insert into posts_comments(post_id, comment_id) values(?, ?)", postId, insertedId);

    }

    @Transactional
    @Override
    public void deleteCommentById(int commentId) {
        jdbcTemplate.update("delete from comments where comments.id=?", commentId);
        jdbcTemplate.update("delete from posts_comments where comment_id = ?", commentId);
    }



    @Override
    public List<Post> findAllByTagOfDefault(String tag, int pageSize, int pageNumber) {
        if(tag.isEmpty()){
            return jdbcTemplate.query(
                    "select id, title, text, imageUrl,likes from posts limit ? offset ?",
                    new Object[]{pageSize, (pageNumber-1)*pageSize},
                    (rs, rowNum) -> new Post(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("text"),
                            rs.getString("imageUrl"),
                            rs.getInt("likes"),
                            getTagsByPostId(rs.getInt("id")),
                            getCommentsByPostId(rs.getInt("id"))
                    ));
        }
        return jdbcTemplate.query("select posts.id, title, text, imageUrl,likes from posts join posts_tags on posts.id = posts_tags.post_id join tags on tag_id=tags.id where tags.tag=? limit ? offset ?",
                new Object[]{tag, pageSize, (pageNumber-1)*pageSize},
                (rs, rowNum) -> new Post(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("text"),
                        rs.getString("imageUrl"),
                        rs.getInt("likes"),
                        getTagsByPostId(rs.getInt("id")),
                        getCommentsByPostId(rs.getInt("id"))
                ));
    }

    @Override
    public Paging getPaging(String tag, int pageSize, int pageNumber){
        if(tag.isEmpty()){
            return jdbcTemplate.query("select count(*) as cnt from posts",
                    rs -> {
                        if (rs.next()) {
                            int count = rs.getInt(1);
                            boolean hasNext = count > pageSize * pageNumber;
                            boolean hasPrevious = pageNumber != 1;
                            return new Paging(pageSize, pageNumber, hasNext, hasPrevious);
                        }
                        else {
                            return null;
                        }
                    }
            );

        }
        return jdbcTemplate.query("select count(*) as cnt from posts join posts_tags on posts.id = posts_tags.post_id join tags on tag_id=tags.id where tags.tag=?",
                new Object[]{tag},
                rs -> {
                    int count = rs.getInt("cnt");
                    boolean hasNext= count > pageSize * pageNumber;
                    boolean hasPrevious = pageNumber==1;
                    return new Paging(pageSize,pageNumber,hasNext,hasPrevious);
                }
                );
    }

}
