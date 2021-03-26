package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.model.Post;
import com.imyourbuddy.petwebapp.repository.PostRepository;
import com.imyourbuddy.petwebapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {
    private PostService postService;
    private PostRepository postRepository;
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        postRepository = Mockito.mock(PostRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        postService = new PostService(postRepository, userRepository);
    }

    @Test
    void save() {
        Post post = new Post("title", "description", "text", 1);
        when(postRepository.save(post)).thenReturn(post);
        Post save = postService.save(post);
        assertTrue(save.getTitle().equals(post.getTitle()));
        assertTrue(save.getDescription().equals(post.getDescription()));
        assertTrue(save.getText().equals(post.getText()));
        assertTrue(save.getAuthor() == post.getAuthor());
        assertTrue(save.getCreatedDate() != null);
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void edit() {
    }

}
