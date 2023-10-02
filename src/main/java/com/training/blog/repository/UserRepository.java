package com.training.blog.repository;

import com.training.blog.models.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    List<User> findAll();

    User save(User user);

    User getReferenceById(Long id);

    Optional<User> findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    void delete(User user);

    Optional<User> findByUsername(String username);
}
