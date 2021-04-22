package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.dto.request.DeletePostRequest;
import com.imyourbuddy.petwebapp.exception.IllegalOperationException;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Mark;
import com.imyourbuddy.petwebapp.model.Post;
import com.imyourbuddy.petwebapp.model.PostImage;
import com.imyourbuddy.petwebapp.model.projection.PostQueryResult;
import com.imyourbuddy.petwebapp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * REST controller for {@link Post}
 */

@RestController
@RequestMapping("/rest/post")
@CrossOrigin(origins = "*")
public class PostController {
    private final PostService service;

    @Autowired
    public PostController(PostService service) {
        this.service = service;
    }

    @GetMapping()
    public List<PostQueryResult> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable(name = "id") long id) throws ResourceNotFoundException, IllegalOperationException {
        Post post = service.getPostById(id);
        return ResponseEntity.ok().body(post);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<PostImage> getImageByPostId(@PathVariable(name = "id") long id) throws ResourceNotFoundException, IllegalOperationException {
        PostImage postImageByPostId = service.getPostImageByPostId(id);
        return ResponseEntity.ok().body(postImageByPostId);
    }

    @GetMapping("/author/{author}")
    @PreAuthorize("hasRole('EXPERT') or hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public List<Post> getPostsByAuthor(@PathVariable(name = "author") long author) throws ResourceNotFoundException {
        return service.getPostsByAuthor(author);
    }
    @PostMapping()
    @PreAuthorize("hasRole('EXPERT') or hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<Post> addNew(@RequestParam(value="file", required=false) MultipartFile image,
                                       @RequestPart(value = "post") @Valid Post post) throws ResourceNotFoundException {
        Post newPost = service.save(post, image);
        return ResponseEntity.ok().body(newPost);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EXPERT') or hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<Post> editPost(@PathVariable(name = "id") long id,
                                         @RequestPart(value = "post") @Valid Post post,
                                         @RequestParam(value="file", required=false) MultipartFile image) throws ResourceNotFoundException, IllegalOperationException {
        Post newPost = service.edit(id, post, image);
        return ResponseEntity.ok().body(newPost);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('EXPERT') or hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public Post deletePost(@PathVariable(name = "id") long id,
                           @RequestBody DeletePostRequest deleted) throws ResourceNotFoundException {
        return service.delete(id, deleted);
    }

    @GetMapping("/moder")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public List<Post> getAllForModer() {
        return service.getAllForModer();
    }

    @GetMapping("/moder/{id}")
    @PreAuthorize("hasRole('EXPERT') or hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<Post> getPostByIdForModer(@PathVariable(name = "id") long id, HttpServletRequest request) throws ResourceNotFoundException, IllegalOperationException {
        String token = request.getHeader("Authorization");
        Post post = service.getDeletedPostById(id, token.substring(7, token.length()));
        return ResponseEntity.ok().body(post);
    }

    @DeleteMapping("/moder/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<Post> deletePostByModer(@PathVariable(name = "id") long id) throws ResourceNotFoundException, IllegalOperationException {
        Post post = service.deletePostByModer(id);
        return ResponseEntity.ok().body(post);
    }

    @PostMapping("/{postId}/rate")
    @PreAuthorize("hasRole('OWNER')")
    public void ratePost(@PathVariable(name = "postId") long postId,
            @RequestBody Mark mark) throws ResourceNotFoundException, IllegalOperationException {
        service.ratePost(postId, mark);
    }

    @GetMapping("/rate/{postId}/{userId}")
    @PreAuthorize("hasRole('OWNER')")
    public Mark checkMark(@PathVariable(name = "postId") long postId,
                          @PathVariable(name = "userId") long userId) throws ResourceNotFoundException, IllegalOperationException {
        return service.checkMark(postId, userId);
    }





}
