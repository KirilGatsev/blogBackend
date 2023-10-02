package com.training.blog.services;

import com.training.blog.exceptions.NotUniqueException;
import com.training.blog.exceptions.PostNotFoundException;
import com.training.blog.exceptions.UserNotFoundException;
import com.training.blog.models.Post;
import com.training.blog.models.User;
import com.training.blog.repository.PostRepository;
import com.training.blog.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    PostRepository postRepository;
    UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Post getPost(Long id){
        return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
    }

    public Post createPost(Post post, Long id){
        User author = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if(postRepository.findByTitle(post.getTitle()).isEmpty()){
            post.setAuthor(author);
            postRepository.save(post);
            return post;
        }else{
            throw new NotUniqueException("Post with title '" + post.getTitle() + "' already exists");
        }
    }

    public Post editPost(Post updatedPost){
        Post oldPost = postRepository.findById(updatedPost.getId()).orElseThrow(
                () -> new PostNotFoundException(updatedPost.getId()));
        oldPost.setTitle(updatedPost.getTitle());
        oldPost.setBody(updatedPost.getBody());
        return postRepository.save(oldPost);
    }

    public void deletePost(Long post_id){
        Post post = postRepository.findById(post_id).orElse(null);
        postRepository.delete(post);
    }
}
