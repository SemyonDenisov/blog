package ru.yandex.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;


@Getter
@AllArgsConstructor
public class Post {
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

    public List<String> getTags() {
        return Arrays.stream(tags.split(",")).toList();
    }

    public List<String> getTextParts() {
        return Arrays.asList(text.split("\n"));
    }

    public int getLikesCount() {
        return likes;
    }

    public String getTextPreview() {
        return this.text.substring(0, 2);
    }

    public String getTagsAsText() {
        return this.tags;
    }
}
