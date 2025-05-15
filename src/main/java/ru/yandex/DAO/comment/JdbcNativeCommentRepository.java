package ru.yandex.DAO.comment;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Statement;
import java.util.Objects;

@Repository
public class JdbcNativeCommentRepository implements CommentRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcNativeCommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
}
