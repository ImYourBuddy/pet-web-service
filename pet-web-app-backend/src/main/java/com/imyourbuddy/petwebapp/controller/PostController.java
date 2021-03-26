package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Post;
import com.imyourbuddy.petwebapp.model.projection.PostProjection;
import com.imyourbuddy.petwebapp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public List<PostProjection> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable(name = "id") long id) throws ResourceNotFoundException {
        Post post = service.getPostById(id);
        return ResponseEntity.ok().body(post);
    }

    @GetMapping("/author/{author}")
    @PreAuthorize("hasRole('EXPERT') or hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public List<Post> getPostByAuthor(@PathVariable(name = "author") long author) throws ResourceNotFoundException {
        return service.getPostByAuthor(author);
    }

    @PostMapping()
    @PreAuthorize("hasRole('EXPERT') or hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<Post> addNew(@RequestBody Post post) {
        Post newPost = service.save(post);
        return ResponseEntity.ok().body(newPost);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EXPERT') or hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<Post> editPost(@PathVariable(name = "id") long id,@RequestBody Post post) throws ResourceNotFoundException {
        Post newPost = service.edit(id, post);
        return ResponseEntity.ok().body(newPost);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EXPERT') or hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public Post deletePost(@PathVariable(name = "id") long id) throws ResourceNotFoundException {
        return service.delete(id);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('EXPERT') or hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public Post restore(@PathVariable(name = "id") long id) throws ResourceNotFoundException {
        return service.restore(id);
    }

    @GetMapping("/moder")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public List<Post> getAllForModer() {
        return service.getAllForModer();
    }

    @DeleteMapping("/moder/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<Post> deletePostByModer(@PathVariable(name = "id") long id) throws ResourceNotFoundException {
        Post post = service.deletePostByModer(id);
        return ResponseEntity.ok().body(post);
    }


}
