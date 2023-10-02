package com.training.blog.services;

import com.training.blog.exceptions.NotUniqueException;
import com.training.blog.exceptions.UserNotFoundException;
import com.training.blog.models.Post;
import com.training.blog.models.User;
import com.training.blog.repository.PostRepository;
import com.training.blog.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    public UserService(UserRepository userRepository, PostRepository postRepository){
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public User getUser(Long id){
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public User createUser(User user){
        if(userRepository.findByUsername(user.getUsername()).isEmpty()) {
            userRepository.save(user);
        }else{
            throw new NotUniqueException("User with name " + user.getUsername() + " already exists");
        }
        return user;
    }

    public User updateUser(User newUser, Long id){
        User oldUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        oldUser.setUsername(newUser.getUsername());
        return userRepository.save(oldUser);
    }

    public List<Post> getUserPosts(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        List<Post> posts = new ArrayList<>();
        posts = postRepository.findAllByAuthorId(id);
        return posts;
    }

    public void deleteUser(Long id){
        User user = userRepository.findById(id).orElse(null);
        userRepository.delete(user);
    }

}
