package ru.parcticum.yandex.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;


@Entity
@Table(name = "posts")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Integer id;
    @Setter
    private String title;
    @Setter
    private String text;

    @Setter
    @Column(name = "imageurl")
    private String imageUrl;
    @Setter
    private int likes;
    @Setter
    private String tags;

}