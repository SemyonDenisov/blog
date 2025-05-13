package ru.yandex.DTO;


import lombok.AllArgsConstructor;





public record  PostDTO(String title,
                       String text,
                       String imageUrl,
                       String tags){}
