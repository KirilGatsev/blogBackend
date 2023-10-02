package com.training.blog.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.blog.exceptions.NotUniqueException;
import com.training.blog.exceptions.UserNotFoundException;
import com.training.blog.models.Post;
import com.training.blog.models.User;
import com.training.blog.repository.PostRepository;
import com.training.blog.repository.UserRepository;
import com.training.blog.services.UserService;
import  org.junit.jupiter.api.Assertions;
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

//Consider defining a bean of type 'com.training.blog.repository.UserRepository' in your configuration.
//probably because i init an example db in main, create separate service and init that db there
//then try removing the unneeded mockbeans

@WebMvcTest(UserController.class)
@EnableWebMvc
class UserControllerTest {
    private static final String END_POINT_PATH = "/api/users";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PostRepository postRepository;
    @MockBean
    private UserService userService;

    @Test
    void testGetUsersShouldReturn200Ok() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void testGetUserThatDoesExistShouldReturn200Ok() throws Exception {
        String requestBody = objectMapper.writeValueAsString(Mockito.mock(User.class));
        Mockito.when(userService.getUser(Mockito.anyLong())).thenReturn(Mockito.mock(User.class));
        mockMvc.perform(MockMvcRequestBuilders
                .get(END_POINT_PATH + "/" + Mockito.anyLong()))
                .andExpect(status().isOk())
                .andExpect(content().json(requestBody))
                .andDo(print());
    }

    @Test
    void testGetUserThatDoesNotExistShouldReturn400BadRequest() throws Exception {
        Mockito.when(userService.getUser(Mockito.anyLong())).thenThrow(UserNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .get(END_POINT_PATH + "/" + Mockito.anyLong()))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andDo(print());
    }

    @Test
    void testGetPostsOfUserThatExist() throws Exception {
        List<Post> posts = new ArrayList<>();
        posts.add(Mockito.mock(Post.class));
        String expectedBody = objectMapper.writeValueAsString(posts);
        Mockito.when(userService.getUserPosts(Mockito.anyLong())).thenReturn(posts);
        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH + "/" + Mockito.anyLong() + "/posts"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBody))
                .andDo(print());
    }

    @Test
    void testGetPostsOfUserThatDoesNotExist() throws Exception {
        Mockito.when(userService.getUserPosts(Mockito.any())).thenThrow(UserNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH + "/" + Mockito.anyLong() + "/posts"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andDo(print());
    }

    @Test
    void testCreateUserShouldReturn201Created() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        String requestBody = objectMapper.writeValueAsString(user);
        Mockito.when(userService.createUser(Mockito.any(User.class)))
                        .thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().json(requestBody))
                .andDo(print());
    }

    @Test
    void testCreateUserNotUniqueNameShouldReturn409Conflict() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        String requestBody = objectMapper.writeValueAsString(user);
        Mockito.when(userService.createUser(Mockito.any())).thenThrow(NotUniqueException.class);
        mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof NotUniqueException))
                .andDo(print());
    }

    @Test
    void testCreateUserEmptyUsernameShouldReturn400BadRequest() throws Exception {
        User user = Mockito.mock(User.class);
        String requestBody = objectMapper.writeValueAsString(user);
        mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andDo(print());
    }

    @Test
    void testUpdateUserIfUserExistsShouldReturn200Ok() throws Exception{
        User user = Mockito.mock(User.class);
        String requestBody = objectMapper.writeValueAsString(user);
        Mockito.when(userService.updateUser(Mockito.any(User.class), Mockito.anyLong())).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.put(END_POINT_PATH + "/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(requestBody))
                .andDo(print());
    }

    @Test
    void testUpdateUserIfUserDoesNotExistShouldReturn400BadRequest() throws Exception{
        User user = Mockito.mock(User.class);
        String requestBody = objectMapper.writeValueAsString(user);
        Mockito.when(userService.updateUser(Mockito.any(User.class), Mockito.anyLong()))
                .thenThrow(UserNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.put(END_POINT_PATH + "/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof UserNotFoundException))
                .andDo(print());
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete(END_POINT_PATH + "/0"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}