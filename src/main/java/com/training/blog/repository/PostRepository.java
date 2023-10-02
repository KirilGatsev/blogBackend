package com.training.blog.repository;

import com.training.blog.models.Post;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {

    List<Post> findAll();
    Post save(Post post);

    Optional<Post> findById(Long id);

    void deleteById(Long id);

    void delete(Post post);

    Optional<Post> findByTitle(String title);

    List<Post> findAllByAuthorId(Long id);
}
