package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Mark;
import com.imyourbuddy.petwebapp.model.Post;
import com.imyourbuddy.petwebapp.model.projection.PostProjection;
import com.imyourbuddy.petwebapp.repository.MarkRepository;
import com.imyourbuddy.petwebapp.repository.PetExpertRepository;
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
    private final MarkRepository markRepository;
    private final PetExpertRepository petExpertRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, MarkRepository markRepository,
                       PetExpertRepository petExpertRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.markRepository = markRepository;
        this.petExpertRepository = petExpertRepository;
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

        post.setTitle(updatedPost.getTitle());
        post.setDescription(updatedPost.getDescription());
        post.setText(updatedPost.getText());

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
        postRepository.restore(id);
        return post;
    }

    public List<Post> getAllForModer() {
        return postRepository.findAllForModer();
    }

    public Post deletePostByModer(long id) throws ResourceNotFoundException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id = " + id + " not found"));
        List<Mark> marks = markRepository.findByPostId(id);
        marks.forEach(markRepository::delete);
        postRepository.delete(post);
        return post;
    }

    public void ratePost(Mark mark) throws ResourceNotFoundException {
        userRepository.findById(mark.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with id = " + mark.getUserId() + " not found"));
        Post post = postRepository.findById(mark.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post with id = " + mark.getPostId() + " not found"));
        markRepository.save(mark);
        if (mark.isLiked()) {
            petExpertRepository.increaseReputation(post.getAuthor());
            postRepository.increaseRating(mark.getPostId());
        } else {
            petExpertRepository.decreaseReputation(post.getAuthor());
            postRepository.decreaseRating(mark.getPostId());
        }
    }

    public Mark checkMark(long postId, long userId) {
        return markRepository.findByPostIdAndUserId(postId, userId);
    }

}
