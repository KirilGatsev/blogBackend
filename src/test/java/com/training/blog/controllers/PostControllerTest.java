package com.training.blog.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.blog.exceptions.PostNotFoundException;
import com.training.blog.exceptions.UserNotFoundException;
import com.training.blog.models.Post;
import com.training.blog.models.User;
import com.training.blog.repository.PostRepository;
import com.training.blog.repository.UserRepository;
import com.training.blog.services.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

//Consider defining a bean of type 'com.training.blog.repository.UserRepository' in your configuration.
//probably because i init an example db in main, create separate service and init that db there
//then try removing the unneeded mockbeans

@WebMvcTest(PostController.class)
@EnableWebMvc
class PostControllerTest {
    private static final String END_POINT_PATH = "/api/posts";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PostRepository postRepository;

    @MockBean
    private PostService postService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testGetPostThatDoesExistShouldReturn200Ok() throws Exception{
        String requestBody = objectMapper.writeValueAsString(Mockito.mock(Post.class));
        Mockito.when(postService.getPost(Mockito.anyLong())).thenReturn(Mockito.mock(Post.class));
        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH + "/" + Mockito.anyLong()))
                .andExpect(status().isOk())
                .andExpect(content().json(requestBody))
                .andDo(print());
    }

    @Test
    void testGetPostThatDoesNotExistShouldReturn400BadRequest() throws Exception{
        Mockito.when(postService.getPost(Mockito.anyLong())).thenThrow(PostNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH + "/" + Mockito.anyLong()))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof PostNotFoundException))
                .andDo(print());
    }

    @Test
    void testListAllPosts() throws Exception{
        List<Post> posts = new ArrayList<>();
        posts.add(Mockito.mock(Post.class));
        String requestBody = objectMapper.writeValueAsString(posts);
        Mockito.when(postRepository.findAll()).thenReturn(posts);
        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(requestBody))
                .andDo(print());
    }

    @Test
    void testCreatePostWhenUserExistAndPostIsValidShouldReturn201Created() throws Exception{
        //not mocked because of @Valid annotation, not empty title and body required
        Post post = new Post();
        post.setTitle("testTitle");
        post.setBody("testBody");
        String requestBody = objectMapper.writeValueAsString(post);
        Mockito.when(postService.createPost(Mockito.any(Post.class), Mockito.anyLong()))
                        .thenReturn(post);
        mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().json(requestBody))
                .andDo(print());
    }

    @Test
    void testCreatePostWhenUserExistAndPostIsNotValidShouldReturn400BadRequest() throws Exception {
        Post post = Mockito.mock(Post.class);
        String requestBody = objectMapper.writeValueAsString(post);
        Mockito.when(postService.createPost(Mockito.any(Post.class), Mockito.anyLong()))
                .thenReturn(post);
        mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andDo(print());
    }

    @Test
    void testCreatePostIsValidAndUserDoesNotExistShouldReturn400BadRequest() throws Exception {
        Post post = new Post();
        post.setTitle("testTitle");
        post.setBody("testBody");
        Mockito.when(postService.createPost(post, 1L)).thenThrow(UserNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andDo(print());
    }

    @Test
    void testCreatePostWhenPostIsNotValidAndUserExistsShouldReturn400BadRequest() throws Exception {
        Post post = Mockito.mock(Post.class);
        //to illustrate that it doesn't even reach below function
        Mockito.when(postService.createPost(Mockito.any(Post.class), Mockito.anyLong())).thenThrow(UserNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andDo(print());
    }

    @Test
    void testUpdatePostWhenPostExistShouldReturn200Ok() throws Exception{
        Post updatedPost = new Post();
        updatedPost.setTitle("testTitle");
        updatedPost.setBody("testBody");
        String requestBody = objectMapper.writeValueAsString(updatedPost);
        Mockito.when(postService.editPost(Mockito.any(Post.class)))
                .thenReturn(updatedPost);
        mockMvc.perform(MockMvcRequestBuilders.put(END_POINT_PATH + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(requestBody))
                .andDo(print());
    }

    @Test
    void testUpdatePostWhenPostDoesNotExistShouldReturn400BadRequest() throws Exception {
        Post updatedPost = new Post();
        updatedPost.setTitle("testTitle");
        updatedPost.setBody("testBody");
        String requestBody = objectMapper.writeValueAsString(updatedPost);
        Mockito.when(postService.editPost(Mockito.any(Post.class))).thenThrow(PostNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.put(END_POINT_PATH + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof PostNotFoundException))
                .andDo(print());
    }

    @Test
    void testUpdatedPostWhenPostIsNotValidShouldReturn400BadRequest() throws Exception {
        Post updatedPost = Mockito.mock(Post.class);
        String requestBody = objectMapper.writeValueAsString(updatedPost);
        //this is here to illustrate that we don't even get to this point with invalid parameters
        Mockito.when(postService.editPost(Mockito.any(Post.class))).thenThrow(PostNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.put(END_POINT_PATH + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andDo(print());
    }

    @Test
    void testDeletePostShouldReturn204NoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(END_POINT_PATH + "/1"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}