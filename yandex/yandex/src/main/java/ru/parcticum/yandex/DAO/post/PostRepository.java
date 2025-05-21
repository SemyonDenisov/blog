package ru.parcticum.yandex.DAO.post;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.parcticum.yandex.DTO.PostDTO;
import ru.parcticum.yandex.model.Post;
import ru.parcticum.yandex.paging.Paging;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @NonNull
    Page<Post> findAll(@NonNull Pageable pageable);

    void deleteById(int id);

    Optional<Post> findById(int id);


    Page<Post> findAllByTagsContaining(String tag,Pageable pageable);
    //List<Post> findAllByTagOfDefault(String tag,int pageSize,int pageNumber);

//    Paging getPaging(String tag, int pageSize, int pageNumber);
}

