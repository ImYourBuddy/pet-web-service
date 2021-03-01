package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.model.Post;
import com.imyourbuddy.petwebapp.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
