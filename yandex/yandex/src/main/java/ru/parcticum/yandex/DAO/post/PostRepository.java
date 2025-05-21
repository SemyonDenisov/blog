package ru.parcticum.yandex.DAO.post;

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
    List<Post> findAll();

    void deleteById(int id);

    Optional<Post> findById(int id);


    List<Post> findAllByTagsContaining(String tag);
    //List<Post> findAllByTagOfDefault(String tag,int pageSize,int pageNumber);

//    Paging getPaging(String tag, int pageSize, int pageNumber);
}

