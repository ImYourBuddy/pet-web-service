package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Post;
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
public class PostController {
    private final PostService service;

    @Autowired
    public PostController(PostService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public List<Post> getAll() {
        return service.getAll();
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('EXPERT') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Post> addNew(@PathVariable Post post) {
        Post newPost = service.save(post);
        return ResponseEntity.ok().body(newPost);
    }

    @PatchMapping("/edit/{id}")
    @PreAuthorize("hasRole('EXPERT') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Post> editPost(@PathVariable(name = "id") long id,@PathVariable Post post) throws ResourceNotFoundException {
        Post newPost = service.edit(id, post);
        return ResponseEntity.ok().body(newPost);
    }


}
