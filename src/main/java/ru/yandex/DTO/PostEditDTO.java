package ru.yandex.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@AllArgsConstructor
@Getter
public class PostEditDTO {
    private int id;
    private String title;
    private String text;
    private MultipartFile image;
    private String tags;
}
