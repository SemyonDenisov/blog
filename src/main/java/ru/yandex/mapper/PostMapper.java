package ru.yandex.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.DTO.PostDTO;
import ru.yandex.model.Post;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostMapper {
    Post mapToPost(PostDTO postDTO);
}
