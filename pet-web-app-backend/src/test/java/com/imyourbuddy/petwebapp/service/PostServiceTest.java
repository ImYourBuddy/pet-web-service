package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.exception.IllegalOperationException;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Post;
import com.imyourbuddy.petwebapp.model.PostImage;
import com.imyourbuddy.petwebapp.model.User;
import com.imyourbuddy.petwebapp.model.projection.PostQueryResult;
import com.imyourbuddy.petwebapp.repository.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.mockito.ArgumentMatchers.any;

class PostServiceTest {
    @InjectMocks
    private PostService postService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserService userService;
    @Mock
    private PetExpertRepository petExpertRepository;
    @Mock
    private MarkRepository markRepository;
    @Mock
    private PostImageRepository imageRepository;

    ProjectionFactory factory = new SpelAwareProxyProjectionFactory();


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllTest() {
        List<PostQueryResult> posts = new ArrayList<>();

        PostQueryResult firstPost = factory.createProjection(PostQueryResult.class);
        PostQueryResult secondPost = factory.createProjection(PostQueryResult.class);

        firstPost.setId(1L);
        firstPost.setTitle("First test post");
        firstPost.setDescription("First test post description");
        firstPost.setText("First test post text");
        firstPost.setAuthor("Test user");
        firstPost.setCreatedDate(new Date());
        firstPost.setDeleted(false);

        secondPost.setId(2L);
        secondPost.setTitle("Second test post");
        secondPost.setDescription("Second test post description");
        secondPost.setText("Second test post text");
        secondPost.setAuthor("Test user");
        secondPost.setCreatedDate(new Date());
        secondPost.setDeleted(true);

        posts.add(secondPost);
        posts.add(firstPost);

        when(postRepository.findAllInOrderByDate()).thenReturn(posts);

        List<PostQueryResult> allPosts = postService.getAll();
        assertEquals(1, allPosts.size());
        assertEquals(firstPost, allPosts.get(0));
        verify(postRepository).findAllInOrderByDate();
    }

    @Test()
    public void getPostByIdTestWithResourceNotFoundException() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> postService.getPostById(1L));
        verify(postRepository).findById(1L);
    }

    @Test()
    public void getPostByIdTestWithIllegalOperationException() throws ResourceNotFoundException, IllegalOperationException {
        Post post = new Post("Test post", "Test post description", "test post text", 1L);
        post.setDeleted(true);

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        assertThrows(IllegalOperationException.class, () -> postService.getPostById(1L));
        verify(postRepository).findById(1L);
    }

    @Test
    public void saveWithoutErrors() {
//        Post post = new Post("title", "description", "text", 1);
//        when(postRepository.save(post)).thenReturn(post);
//        MultipartFile image = mock(MultipartFile.class);
//        Post save = postService.save(post, image);
//        assertTrue(save.getTitle().equals(post.getTitle()));
//        assertTrue(save.getDescription().equals(post.getDescription()));
//        assertTrue(save.getText().equals(post.getText()));
//        assertTrue(save.getAuthor() == post.getAuthor());
//        assertTrue(save.getCreatedDate() != null);
//        verify(postRepository, times(1)).save(post);
    }

    @Test
    public void edit() {
    }

    @Test
    public void getPostsByAuthorTestWithResourceNotFoundException() throws ResourceNotFoundException {
        when(userService.getById(anyLong())).thenThrow(new ResourceNotFoundException("User not found"));
        assertThrows(ResourceNotFoundException.class, () -> userService.getById(1L));
        verify(userService).getById(1L);
    }

    @Test
    public void getPostsByAuthorTest() throws ResourceNotFoundException {
        Post firstPost = new Post("First test post", "First test post description",
                "First test post text", 1L);
        Post secondPost = new Post("Second test post", "Second test post description",
                "Second test post text", 1L);
        List<Post> postList = new ArrayList<>();
        postList.add(firstPost);
        postList.add(secondPost);

        when(userService.getById(anyLong())).thenReturn(new User());
        when(postRepository.findAllByAuthor(1L)).thenReturn(postList);

        postService.getPostsByAuthor(1L);

        verify(userService).getById(1L);
        verify(postRepository).findAllByAuthor(1L);
        assertEquals(2, postList.size());
    }
}
