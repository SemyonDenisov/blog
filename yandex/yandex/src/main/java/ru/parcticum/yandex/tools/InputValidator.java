package ru.parcticum.yandex.tools;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

public class InputValidator {
    public static boolean isValidImage(MultipartFile image) {
        if (image == null) return false;
        String extension = Objects.requireNonNull(image.getOriginalFilename()).split("\\.")[1];
        return List.of("jpg", "jpeg", "png").contains(extension);
    }

    public static boolean isValidComment(String text) {
        return text != null && !text.isEmpty();
    }

}
