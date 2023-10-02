package com.training.blog.controllers;

import com.training.blog.exceptions.PostNotFoundException;
import com.training.blog.models.Post;
import com.training.blog.repository.PostRepository;
import com.training.blog.services.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/posts")
public class PostController {

    private final PostRepository postRepository;

    private final PostService postService;

    public PostController(PostRepository postRepository,
                          PostService postService){
        this.postRepository = postRepository;
        this.postService = postService;
    }

    @GetMapping("")
    ResponseEntity<List<Post>> getAllPosts(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(postRepository.findAll());
    }

    @GetMapping("/{post_id}")
    ResponseEntity<Post> getPost(@PathVariable Long post_id){
        Post post = postService.getPost(post_id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(post);
    }

    @PostMapping("/{user_id}")
    ResponseEntity<Post> createPost (@RequestBody @Valid Post post,
                     @PathVariable(name = "user_id") Long id){
        Post createdPost = postService.createPost(post, id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdPost);
    }

    @PutMapping("/{post_id}")
    ResponseEntity<Post> updatePost(@RequestBody @Valid Post post,@PathVariable Long post_id){
        Post updatedPost = postService.editPost(post);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedPost);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{post_id}")
    void deletePost(@PathVariable Long post_id){
        postService.deletePost(post_id);
    }

}
