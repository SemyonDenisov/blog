package ru.yandex.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;



@Getter
@AllArgsConstructor
public class Post {
    private int id;
    private String title;
    private String text;
    private String imageUrl;
    private int likes;
    private List<String> tags;


    public int getLikesCount() {
        return likes;
    }
    public String getTextPreview(){
        return this.text.substring(0,2);
    }
}
