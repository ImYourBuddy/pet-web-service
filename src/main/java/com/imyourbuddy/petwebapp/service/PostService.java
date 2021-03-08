package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Post;
import com.imyourbuddy.petwebapp.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for {@link Post}
 */

@Service
public class PostService {
    private final PostRepository repository;

    @Autowired
    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public List<Post> getAll() {
        return repository.findAll();
    }

    public Post save(Post post) {
        return repository.save(post);
    }

    public Post edit(long id, Post updatedPost) throws ResourceNotFoundException {
        Post post = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id = " + id + " not found"));
        if (updatedPost.getTitle() != null) {
            post.setTitle(updatedPost.getTitle());
        }
        if (updatedPost.getDescription() != null) {
            post.setDescription(updatedPost.getDescription());
        }
        if (updatedPost.getText() != null) {
            post.setText(updatedPost.getText());
        }
        return repository.save(post);
    }
}
