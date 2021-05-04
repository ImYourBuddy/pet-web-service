package com.imyourbuddy.petwebapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imyourbuddy.petwebapp.dto.request.DeletePostRequest;
import com.imyourbuddy.petwebapp.exception.IllegalOperationException;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Mark;
import com.imyourbuddy.petwebapp.model.Post;
import com.imyourbuddy.petwebapp.service.PostService;
import lombok.SneakyThrows;
import lombok.With;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
class PostControllerTest {
    @MockBean
    private PostService postService;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void getPostByIdExceptDeletedTestWithNotFoundWhenResourceNotFoundException() throws Exception {
        when(postService.getPostByIdExceptDeleted(1L))
                .thenThrow(new ResourceNotFoundException("Post with id = 1 not found"));
        mvc.perform(get("/rest/post/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Post with id = 1 not found")));
    }

    @Test
    public void getPostByIdExceptDeletedTestWithNotFoundWhenIllegalOperationException() throws Exception {
        when(postService.getPostByIdExceptDeleted(1L))
                .thenThrow(new IllegalOperationException("Post with id = 1 is deleted"));
        mvc.perform(get("/rest/post/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Post with id = 1 is deleted")));
    }

    @Test
    public void getPostImageByPostIdTestWithNotFoundWhenResourceNotFoundException() throws Exception {
        when(postService.getPostImageByPostId(1L))
                .thenThrow(new ResourceNotFoundException("Post with id = 1 not found"));
        mvc.perform(get("/rest/post/1/image"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Post with id = 1 not found")));
    }

    @Test
    public void addNewPostTestWithUnauthorized() throws Exception {
        Post post = new Post(1, "Test title", "Test description for test", "Test text",
                1, new Date(), 0, false);
        MockMultipartFile image = new MockMultipartFile("file", "filename-1.jpeg",
                "image/jpeg", "some-image".getBytes());
        MockMultipartFile postRequest = new MockMultipartFile("post", "json", "application/json",
                new ObjectMapper().writeValueAsString(post).getBytes());
        mvc.perform(multipart("/rest/post")
                .file(image)
                .file(postRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser("OWNER")
    public void addNewPostTestWithForbidden() throws Exception {
        Post post = new Post(1, "Test title", "Test description for test", "Test text",
                1, new Date(), 0, false);
        MockMultipartFile image = new MockMultipartFile("file", "filename-1.jpeg",
                "image/jpeg", "some-image".getBytes());
        MockMultipartFile postRequest = new MockMultipartFile("post", "json", "application/json",
                new ObjectMapper().writeValueAsString(post).getBytes());
        mvc.perform(multipart("/rest/post")
                .file(image)
                .file(postRequest))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "EXPERT")
    public void addNewPostTestWithNotFound() throws Exception {
        Post post = new Post(1, "Test title", "Test description for test", "Test text",
                1, new Date(), 0, false);
        MockMultipartFile image = new MockMultipartFile("file", "filename-1.jpeg",
                "image/jpeg", "some-image".getBytes());
        MockMultipartFile postRequest = new MockMultipartFile("post", "json", "application/json",
                new ObjectMapper().writeValueAsString(post).getBytes());
        when(postService.save(post, image))
                .thenThrow(new ResourceNotFoundException("User with id = 1 not found"));
        mvc.perform(multipart("/rest/post")
                .file(image)
                .file(postRequest))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id = 1 not found")));
    }

    @Test
    @WithMockUser(roles = "EXPERT")
    public void addNewPostTestWithBadRequest() throws Exception {
        Post post = new Post(1, "T", "T", "Test text",
                1, new Date(), 0, false);
        MockMultipartFile image = new MockMultipartFile("file", "filename-1.jpeg",
                "image/jpeg", "some-image".getBytes());
        MockMultipartFile postRequest = new MockMultipartFile("post", "json", "application/json",
                new ObjectMapper().writeValueAsString(post).getBytes());
        mvc.perform(multipart("/rest/post")
                .file(image)
                .file(postRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @Test
    public void getPostsByAuthorTestWithUnauthorized() throws Exception {
        mvc.perform(get("/rest/post/author/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void getPostsByAuthorTestWithForbidden() throws Exception {
        mvc.perform(get("/rest/post/author/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "EXPERT")
    public void getPostsByAuthorTestWithNotFound() throws Exception {
        when(postService.getPostsByAuthor(1L))
                .thenThrow(new ResourceNotFoundException("User with id = 1 not found"));
        mvc.perform(get("/rest/post/author/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with id = 1 not found")));
    }

    @Test
    public void removeFromPublicAccessTestWithUnauthorized() throws Exception {
        DeletePostRequest request = new DeletePostRequest();
        mvc.perform(patch("/rest/post/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void removeFromPublicAccessTestWithForbidden() throws Exception {
        DeletePostRequest request = new DeletePostRequest();
        mvc.perform(patch("/rest/post/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "EXPERT")
    public void removeFromPublicAccessTestWithNotFoundWhenResourceNotFoundException() throws Exception {
        DeletePostRequest request = new DeletePostRequest(true);
        when(postService.removeFromPublicAccess(1L, request.isDelete()))
                .thenThrow(new ResourceNotFoundException("Post with id = 1 not found"));
        mvc.perform(patch("/rest/post/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Post with id = 1 not found")));
    }

    @Test
    @WithMockUser(roles = "EXPERT")
    public void removeFromPublicAccessTestWithNotFoundWhenIllegalOperationException() throws Exception {
        DeletePostRequest request = new DeletePostRequest(true);
        when(postService.removeFromPublicAccess(1L, request.isDelete()))
                .thenThrow(new IllegalOperationException("Post is already deleted or restored"));
        mvc.perform(patch("/rest/post/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Post is already deleted or restored")));
    }

    @Test
    public void getAllTestWithUnauthorized() throws Exception {
        mvc.perform(get("/rest/post/all"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EXPERT"})
    public void getAllTestWithForbidden() throws Exception {
        mvc.perform(get("/rest/post/all"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getPostByIdTestWithUnauthorized() throws Exception {
        mvc.perform(get("/rest/post/all/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void getPostByIdTestWithForbidden() throws Exception {
        mvc.perform(get("/rest/post/all/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "EXPERT")
    public void getPostByIdTestWithNotFoundWhenResourceNotFoundException() throws Exception {
        when(postService.getPostById(1L, "token"))
                .thenThrow(new ResourceNotFoundException("Post with id = 1 not found"));
        mvc.perform(get("/rest/post/all/1")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Post with id = 1 not found")));
    }

    @Test
    @WithMockUser(roles = "EXPERT")
    public void getPostByIdTestWithNotFoundWhenIllegalOperationException() throws Exception {
        when(postService.getPostById(1L, "token"))
                .thenThrow(new IllegalOperationException("User haven't access to this post"));
        mvc.perform(get("/rest/post/all/1")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User haven't access to this post")));
    }

    @Test
    public void deletePostTestWithUnauthorized() throws Exception {
        mvc.perform(delete("/rest/post/all/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"OWNER", "EXPERT"})
    public void deletePostTestWithForbidden() throws Exception {
        mvc.perform(delete("/rest/post/all/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void deletePostTestWithNotFound() throws Exception {
        when(postService.deletePost(1L))
                .thenThrow(new ResourceNotFoundException("Post with id = 1 not found"));
        mvc.perform(delete("/rest/post/all/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Post with id = 1 not found")));

    }

    @Test
    public void ratePostTestWithUnauthorized() throws Exception {
        mvc.perform(post("/rest/post/1/rate"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void ratePostTestWithNotFoundWhenResourceNotFoundException() throws Exception {
        Mark mark = new Mark(1L, 2L, true);
        doThrow(new ResourceNotFoundException("Post with id = 1 not found"))
                .when(postService).ratePost(1L, mark);
        mvc.perform(post("/rest/post/1/rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(mark)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Post with id = 1 not found")));
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void ratePostTestWithNotFoundWhenIllegalOperationException() throws Exception {
        Mark mark = new Mark(1L, 2L, true);
        doThrow(new ResourceNotFoundException("Post with id = 1 is deleted"))
                .when(postService).ratePost(1L, mark);
        mvc.perform(post("/rest/post/1/rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(mark)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Post with id = 1 is deleted")));
    }

    @Test
    public void checkMarkTestWithUnauthorized() throws Exception {
        mvc.perform(get("/rest/post/rate/1/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "OWNER")
    public void checkMarkTestWithNotFound() throws Exception {
        when(postService.checkMark(1L, 1L))
                .thenThrow(new ResourceNotFoundException("Post with id = 1 not found"));
        mvc.perform(get("/rest/post/rate/1/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Post with id = 1 not found")));
    }
}
