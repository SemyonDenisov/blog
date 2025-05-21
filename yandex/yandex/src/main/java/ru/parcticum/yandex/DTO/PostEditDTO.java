package ru.parcticum.yandex.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;


@AllArgsConstructor
@Getter
public class PostEditDTO {
    private int id;
    private String title;
    private String text;
    private MultipartFile image;
    private String tags;
}
