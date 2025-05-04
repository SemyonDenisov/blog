package ru.yandex.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



@Getter
@AllArgsConstructor
public class Post {
    private int id;
    private String title;
    private String text;
    private String imageUrl;
    private int likes;
    private List<Tag> tags;
    private List<Comment> comments;

    public List<String> getTags(){
        return tags.stream().map(Tag::getTag).toList();
    }
    public List<String> getTextParts() {
        return Arrays.asList(text.split("\n"));
    }
    public int getLikesCount() {
        return likes;
    }
    public String getTextPreview(){
        return this.text.substring(0,2);
    }
}
