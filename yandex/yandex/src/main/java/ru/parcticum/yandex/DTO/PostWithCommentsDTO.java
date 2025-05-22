package ru.parcticum.yandex.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.parcticum.yandex.model.Comment;
import ru.parcticum.yandex.model.Post;

import java.util.Arrays;
import java.util.List;


@AllArgsConstructor
@Getter
public class PostWithCommentsDTO {
    private int id;
    @Setter
    private String title;
    @Setter
    private String text;
    @Setter
    private String imageUrl;
    @Setter
    private int likes;
    @Setter
    private String tags;
    @Setter
    private List<Comment> comments;

    public PostWithCommentsDTO(Post post, List<Comment> comments) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.text = post.getText();
        this.imageUrl = post.getImageUrl();
        this.likes = post.getLikes();
        this.tags = post.getTags();
        this.comments = comments;
    }

    public List<String> getTagsList() {
        return Arrays.stream(tags.split(",")).toList();
    }

    public List<String> getTextParts() {
        return Arrays.asList(text.split("\n"));
    }

    public int getLikesCount() {
        return likes;
    }

    public String getTextPreview() {
        if (text.length() < 20) {
            return text;
        } else {
            return Arrays.stream(text.split(" ")).limit(5).reduce("", (a, b) -> a + " " + b);
        }
    }

    public String getTagsAsText() {
        return this.tags;
    }
}
