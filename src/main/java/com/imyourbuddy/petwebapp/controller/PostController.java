package com.imyourbuddy.petwebapp.controller;

import com.imyourbuddy.petwebapp.model.Post;
import com.imyourbuddy.petwebapp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public List<Post> getAll() {
        return service.getAll();
    }
}
