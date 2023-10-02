package com.training.blog.controllers;

import com.training.blog.exceptions.NotUniqueException;
import com.training.blog.exceptions.UserNotFoundException;
import com.training.blog.models.Post;
import com.training.blog.models.User;
import com.training.blog.repository.PostRepository;
import com.training.blog.repository.UserRepository;
import com.training.blog.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(PostRepository postRepository, UserRepository userRepository,
                          UserService userService){
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("")
    ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(userRepository.findAll());
    }


    @GetMapping("/{user_id}")
    ResponseEntity<User> getUser(@PathVariable(name = "user_id") Long id){
        User user = userService.getUser(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(user);
    }

    @GetMapping("/{user_id}/posts")
    ResponseEntity<List<Post>> getUserPosts(@PathVariable(name = "user_id")Long id){
        List<Post> posts = userService.getUserPosts(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(posts);
    }

    @PostMapping("")
    ResponseEntity<User> createUser(@RequestBody @Valid User user){
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
               .body(createdUser);
    }

    @PutMapping("/{user_id}")
    ResponseEntity<User> userUpdate(@PathVariable(name = "user_id") Long id, @RequestBody User newUser){
        User updatedUser = userService.updateUser(newUser, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedUser);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{user_id}")
    void deleteUser(@PathVariable(name = "user_id") Long id){
        userService.deleteUser(id);
    }

}
