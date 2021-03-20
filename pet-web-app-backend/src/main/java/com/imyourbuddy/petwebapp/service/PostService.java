package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Post;
import com.imyourbuddy.petwebapp.model.projection.PostProjection;
import com.imyourbuddy.petwebapp.repository.PostRepository;
import com.imyourbuddy.petwebapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for {@link Post}
 */

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<PostProjection> getAll() {
        List<PostProjection> allInOrderByDate = postRepository.findAllInOrderByDate();
        List<PostProjection> result = allInOrderByDate.stream()
                .filter(post -> post.getDeleted() == false)
                .collect(Collectors.toList());
        return result;
    }

    public Post getPostById(long id) throws ResourceNotFoundException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id = " + id + " not found"));
        return post;
    }

    public Post save(Post post) {
        Timestamp createdDate = new Timestamp(new Date().getTime());
        post.setCreatedDate(createdDate);
        return postRepository.save(post);
    }

    public Post edit(long id, Post updatedPost) throws ResourceNotFoundException {
        Post post = postRepository.findById(id)
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
        return postRepository.save(post);
    }

    public List<Post> getPostByAuthor(long id) throws ResourceNotFoundException {
        userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id = " + id + " not found"));
        return postRepository.findAllByAuthor(id);
    }

    public Post delete(long id) throws ResourceNotFoundException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id = " + id + " not found"));
        postRepository.delete(id);
        return post;
    }

    public Post restore(long id) throws ResourceNotFoundException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id = " + id + " not found"));
        postRepository.unDelete(id);
        return post;
    }

    public List<Post> getAllModer() {
        return postRepository.findAllModer();
    }

    public Post deletePostByModer(long id) throws ResourceNotFoundException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id = " + id + " not found"));
        postRepository.delete(post);
        return post;
    }

}
