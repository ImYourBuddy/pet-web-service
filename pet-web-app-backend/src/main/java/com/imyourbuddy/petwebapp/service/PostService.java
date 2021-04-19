package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.dto.request.DeletePostRequest;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.Mark;
import com.imyourbuddy.petwebapp.model.Post;
import com.imyourbuddy.petwebapp.model.PostImage;
import com.imyourbuddy.petwebapp.model.projection.PostQueryResult;
import com.imyourbuddy.petwebapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
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
    private final UserService userService;
    private final MarkRepository markRepository;
    private final PetExpertRepository petExpertRepository;
    private final PostImageRepository imageRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService, MarkRepository markRepository,
                       PetExpertRepository petExpertRepository, PostImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.markRepository = markRepository;
        this.petExpertRepository = petExpertRepository;
        this.imageRepository = imageRepository;
    }

    public List<PostQueryResult> getAll() {
        List<PostQueryResult> allInOrderByDate = postRepository.findAllInOrderByDate();
        List<PostQueryResult> result = allInOrderByDate.stream()
                .filter(post -> !post.getDeleted())
                .collect(Collectors.toList());
        return result;
    }

    public Post getPostById(long id) throws ResourceNotFoundException {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id = " + id + " not found"));
    }

    public PostImage getPostImageByPostId(long id) throws ResourceNotFoundException {
        getPostById(id);
        return imageRepository.getByPostId(id);
    }

    public Post save(Post post, MultipartFile image) throws ResourceNotFoundException {
        userService.getById(post.getAuthor());
        Timestamp createdDate = new Timestamp(new Date().getTime());
        post.setCreatedDate(createdDate);
        postRepository.save(post);
        Post savedPost = postRepository
                .findByAuthorAndAndCreatedDateAndTitle(post.getAuthor(), createdDate, post.getTitle());
        if (image != null) {
            try (BufferedInputStream bis = new BufferedInputStream(image.getInputStream())) {
                PostImage postImage = new PostImage(savedPost.getId(), bis.readAllBytes());
                imageRepository.save(postImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return post;
    }

    public Post edit(long id, Post updatedPost, MultipartFile updatedImage) throws ResourceNotFoundException {
        Post post = getPostById(id);

        post.setTitle(updatedPost.getTitle());
        post.setDescription(updatedPost.getDescription());
        post.setText(updatedPost.getText());
        if (updatedImage != null) {
            try (BufferedInputStream bis = new BufferedInputStream(updatedImage.getInputStream())) {
                PostImage postImage = imageRepository.getByPostId(id);
                if (postImage == null) {
                    PostImage newImage = new PostImage(post.getId(), bis.readAllBytes());
                    imageRepository.save(newImage);
                } else {
                    postImage.setImage(bis.readAllBytes());
                    imageRepository.save(postImage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return postRepository.save(post);
    }

    public List<Post> getPostsByAuthor(long id) throws ResourceNotFoundException {
        userService.getById(id);
        return postRepository.findAllByAuthor(id);
    }

    public Post delete(long id, DeletePostRequest deleted) throws ResourceNotFoundException {
        Post post = getPostById(id);
        if (deleted.isDeleted()) {
            postRepository.delete(id);
        } else {
            postRepository.restore(id);
        }
        return post;
    }

    public List<Post> getAllForModer() {
        return postRepository.findAllForModer();
    }

    public Post deletePostByModer(long id) throws ResourceNotFoundException {
        Post post = getPostById(id);
        List<Mark> marks = markRepository.findByPostId(id);
        marks.forEach(markRepository::delete);
        PostImage postImage = imageRepository.getByPostId(id);
        if (postImage != null) {
            imageRepository.delete(postImage);
        }
        postRepository.delete(post);
        return post;
    }

    public void ratePost(long postId, Mark mark) throws ResourceNotFoundException {
        userService.getById(mark.getUserId());
        Post post = getPostById(postId);
        markRepository.save(mark);
        if (mark.isLiked()) {
            petExpertRepository.increaseReputation(post.getAuthor());
            postRepository.increaseRating(mark.getPostId());
        } else {
            petExpertRepository.decreaseReputation(post.getAuthor());
            postRepository.decreaseRating(mark.getPostId());
        }
    }

    public Mark checkMark(long postId, long userId) throws ResourceNotFoundException {
        userService.getById(userId);
        getPostById(userId);
        return markRepository.findByPostIdAndUserId(postId, userId);
    }

}
