package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.exception.IllegalOperationException;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.*;
import com.imyourbuddy.petwebapp.model.projection.PostQueryResult;
import com.imyourbuddy.petwebapp.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
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
    @Mock
    private RoleRepository roleRepository;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();


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
        verify(postRepository).findAllInOrderByDate();
        assertEquals(1, allPosts.size());
        assertEquals(firstPost.getId(), allPosts.get(0).getId());
        assertEquals(firstPost.getTitle(), allPosts.get(0).getTitle());
        assertEquals(firstPost.getDescription(), allPosts.get(0).getDescription());
        assertEquals(firstPost.getText(), allPosts.get(0).getText());
        assertEquals(firstPost.getAuthor(), allPosts.get(0).getAuthor());
        assertEquals(firstPost.getCreatedDate(), allPosts.get(0).getCreatedDate());
        assertFalse(allPosts.get(0).getDeleted());
    }

    @Test()
    public void getPostByIdTestWithResourceNotFoundException() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> postService.getPostById(1L));
        verify(postRepository).findById(1L);
    }

    @Test()
    public void getPostByIdTestWithIllegalOperationException() {
        Post post = new Post("Test post", "Test post description", "test post text", 1L);
        post.setDeleted(true);

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        assertThrows(IllegalOperationException.class, () -> postService.getPostById(1L));
        verify(postRepository).findById(1L);
    }

    @Test
    public void saveTest() throws ResourceNotFoundException {
        Post post = new Post("Test post", "Test post description", "test post text", 1L);
        Post savedPost = new Post(1L, "Test post", "Test post description", "test post text",
                1L, new Date(),0L, false);
        when(postRepository.save(post)).thenReturn(savedPost);
        MockMultipartFile image = new MockMultipartFile("image", "test image.png", MediaType.IMAGE_PNG_VALUE,
                    "image".getBytes());
        PostImage postImage = null  ;
        try {
            postImage = new PostImage(1L, image.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        postService.save(post, image);
        verify(imageRepository).save(postImage);
    }

    @Test
    public void editPostTestWithResourceNotFoundException() {
        Post updatedPost = new Post(1L, "Test post", "Test post description", "test post text",
                1L, new Date(),0L, false);
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> postService.edit(1L, updatedPost, null));
        verify(imageRepository, never()).save(any());
        verify(imageRepository, never()).findByPostId(anyLong());
        verify(postRepository, never()).save(any());
    }

    @Test
    public void editPostTestWithoutImage() throws ResourceNotFoundException {
        Post post = new Post(1L, "Test post", "Test post description", "test post text",
                1L, new Date(),0L, false);
        Post updatedPost = new Post(1L, "Updated test post", "Updated test post description",
                "Updated test post text", 1L, new Date(),0L, false);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        postService.edit(1L, updatedPost, null);

        verify(imageRepository, never()).save(any());
        verify(imageRepository, never()).findByPostId(anyLong());
        verify(postRepository).save(updatedPost);
    }

    @Test
    public void removeFromPublicAccessTestWithResourceNotFoundException() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> postService.removeFromPublicAccess(1L, anyBoolean()));
        verify(postRepository).findById(1L);
        verify(postRepository, never()).removeFromPublicAccess(anyLong());
        verify(postRepository, never()).restore(anyLong());
    }

    @Test
    public void removeFromPublicAccessTestWithIllegalOperationException() {
        Post post = new Post(1L, "Test post", "Test post description", "test post text",
                1L, new Date(),0L, false);
        Post deletedPost = new Post(1L, "Test post", "Test post description", "test post text",
                1L, new Date(),0L, true);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        assertThrows(IllegalOperationException.class, () -> postService.removeFromPublicAccess(1L, false));

        when(postRepository.findById(1L)).thenReturn(Optional.of(deletedPost));

        assertThrows(IllegalOperationException.class, () -> postService.removeFromPublicAccess(1L, true));
        verify(postRepository, times(2)).findById(1L);
        verify(postRepository, never()).removeFromPublicAccess(anyLong());
        verify(postRepository, never()).restore(anyLong());
    }

    @Test
    public void removeFromPublicAccessDeleteTest() throws ResourceNotFoundException, IllegalOperationException {
        Post post = new Post(1L, "Test post", "Test post description", "test post text",
                1L, new Date(),0L, false);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        postService.removeFromPublicAccess(1L, true);
        verify(postRepository).findById(1L);
        verify(postRepository).removeFromPublicAccess(1L);
        verify(postRepository, never()).restore(1L);
    }

    @Test
    public void removeFromPublicAccessRestoreTest() throws ResourceNotFoundException, IllegalOperationException {
        Post post = new Post(1L, "Test post", "Test post description", "test post text",
                1L, new Date(),0L, true);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        postService.removeFromPublicAccess(1L, false);
        verify(postRepository).findById(1L);
        verify(postRepository, never()).removeFromPublicAccess(1L);
        verify(postRepository).restore(1L);
    }

    @Test
    public void deletePostByModerTestWithResourceNotFoundException() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> postService.deletePostByModer(1L));
        verify(postRepository).findById(1L);
        verify(markRepository, never()).findByPostId(anyLong());
        verify(markRepository, never()).delete(any());
        verify(imageRepository, never()).findByPostId(anyLong());
        verify(imageRepository, never()).delete(any());
    }

    @Test
    public void deletePostByModerTestWithoutException() throws ResourceNotFoundException {
        Post post = new Post(1L, "Test post", "Test post description", "test post text",
                1L, new Date(),0L, false);
        Mark firstMark = new Mark(1L, 2L, true);
        Mark secondMark = new Mark(1L, 3L, false);
        List<Mark> marks = new ArrayList<>();
        marks.add(firstMark);
        marks.add(secondMark);
        PostImage image = new PostImage();
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(markRepository.findByPostId(1L)).thenReturn(marks);
        when(imageRepository.findByPostId(1L)).thenReturn(image);

        postService.deletePostByModer(1L);

        verify(postRepository).findById(1L);
        verify(markRepository).findByPostId(1L);
        verify(markRepository).delete(firstMark);
        verify(markRepository).delete(secondMark);
        verify(imageRepository).findByPostId(1L);
        verify(imageRepository).delete(image);
    }

    @Test
    public void getPostByIdForExpertModerAdminWithResourceNotFoundException() throws ResourceNotFoundException {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> postService.getPostByIdForExpertModerAdmin(1L, "token"));
        verify(postRepository).findById(1L);
        verify(userService, never()).getUserByToken(anyString());
        verify(roleRepository, never()).findByName(anyString());
    }

    @Test
    public void getPostByIdForExpertModerAdminWithIllegalOperationException() throws ResourceNotFoundException {
        Post post = new Post(1L, "Test post", "Test post description", "test post text",
                2L, new Date(),0L, true);
        Role roleAdministrator = new Role(1, "ROLE_ADMINISTRATOR");
        Role roleModerator = new Role(2, "ROLE_MODERATOR");
        Role roleExpert = new Role(3, "ROLE_EXPERT");
        List<Role> roles = new ArrayList<>();
        roles.add(roleExpert);
        User user = new User(1L, "Test", "test1234", "Test", "Test", new Date(),
                false, false, roles);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(userService.getUserByToken("token")).thenReturn(user);
        when(roleRepository.findByName("ROLE_ADMINISTRATOR")).thenReturn(roleAdministrator);
        when(roleRepository.findByName("ROLE_MODERATOR")).thenReturn(roleModerator);
        when(roleRepository.findByName("ROLE_EXPERT")).thenReturn(roleExpert);

        assertThrows(IllegalOperationException.class,
                () -> postService.getPostByIdForExpertModerAdmin(1L, "token"));
        verify(postRepository).findById(1L);
        verify(userService).getUserByToken("token");
        verify(roleRepository).findByName("ROLE_ADMINISTRATOR");
        verify(roleRepository).findByName("ROLE_MODERATOR");
        verify(roleRepository).findByName("ROLE_EXPERT");
    }

    @Test
    public void ratePostTestLike() throws ResourceNotFoundException, IllegalOperationException {
        Post post = new Post(1L, "Test post", "Test post description", "test post text",
                2L, new Date(),0L, false);
        Mark like = new Mark(1L, 1L, true);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        postService.ratePost(1L, like);

        verify(postRepository).findById(1L);
        verify(markRepository).save(like);
        verify(petExpertRepository).increaseReputation(2L);
        verify(petExpertRepository, never()).decreaseReputation(2L);
        verify(postRepository).increaseRating(1L);
        verify(postRepository,never()).decreaseRating(1L);

    }

    @Test
    public void ratePostTestDislike() throws ResourceNotFoundException, IllegalOperationException {
        Post post = new Post(1L, "Test post", "Test post description", "test post text",
                2L, new Date(),0L, false);
        Mark like = new Mark(1L, 1L, false);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        postService.ratePost(1L, like);

        verify(postRepository).findById(1L);
        verify(markRepository).save(like);
        verify(petExpertRepository, never()).increaseReputation(2L);
        verify(petExpertRepository).decreaseReputation(2L);
        verify(postRepository, never()).increaseRating(1L);
        verify(postRepository).decreaseRating(1L);

    }
}
