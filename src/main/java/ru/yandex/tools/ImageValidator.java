package ru.yandex.tools;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ImageValidator {
    public static boolean isValidImage(MultipartFile image){
        String extension = Objects.requireNonNull(image.getOriginalFilename()).split("\\.")[1];
        return List.of("jpg", "jpeg", "png").contains(extension);
    }
}
