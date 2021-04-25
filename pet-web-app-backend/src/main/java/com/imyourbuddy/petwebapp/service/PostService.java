package com.imyourbuddy.petwebapp.service;

import com.imyourbuddy.petwebapp.exception.IllegalOperationException;
import com.imyourbuddy.petwebapp.exception.ResourceNotFoundException;
import com.imyourbuddy.petwebapp.model.*;
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
    private final RoleRepository roleRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService, MarkRepository markRepository,
                       PetExpertRepository petExpertRepository, PostImageRepository imageRepository, RoleRepository roleRepository) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.markRepository = markRepository;
        this.petExpertRepository = petExpertRepository;
        this.imageRepository = imageRepository;
        this.roleRepository = roleRepository;
    }

    public List<PostQueryResult> getAll() {
        List<PostQueryResult> allInOrderByDate = postRepository.findAllInOrderByDate();
        List<PostQueryResult> result = allInOrderByDate.stream()
                .filter(post -> !post.getDeleted())
                .collect(Collectors.toList());
        return result;
    }

    public Post getPostById(long id) throws ResourceNotFoundException, IllegalOperationException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id = " + id + " not found"));
        if (post.isDeleted()) {
            throw new IllegalOperationException("Post with id = " + id + " is deleted");
        }
        return post;
    }

    public PostImage getPostImageByPostId(long id) throws ResourceNotFoundException, IllegalOperationException {
        postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id = " + id + " not found"));
        return imageRepository.findByPostId(id);
    }

    public Post save(Post post, MultipartFile image) throws ResourceNotFoundException {
        userService.getById(post.getAuthor());
        Timestamp createdDate = new Timestamp(new Date().getTime());
        post.setCreatedDate(createdDate);
        Post savedPost = postRepository.save(post);
        if (image != null) {
            try (BufferedInputStream bis = new BufferedInputStream(image.getInputStream())) {
                PostImage postImage = new PostImage(savedPost.getId(), bis.readAllBytes());
                imageRepository.save(postImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return savedPost;
    }

    public Post edit(long id, Post updatedPost, MultipartFile updatedImage) throws ResourceNotFoundException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id = " + id + " not found"));

        post.setTitle(updatedPost.getTitle());
        post.setDescription(updatedPost.getDescription());
        post.setText(updatedPost.getText());
        if (updatedImage != null) {
            try (BufferedInputStream bis = new BufferedInputStream(updatedImage.getInputStream())) {
                PostImage postImage = imageRepository.findByPostId(id);
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

    public Post removeFromPublicAccess(long id, boolean delete) throws ResourceNotFoundException, IllegalOperationException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id = " + id + " not found"));
        if (delete && !post.isDeleted()) {
            postRepository.removeFromPublicAccess(id);
        } else if (!delete && post.isDeleted()) {
            postRepository.restore(id);
        } else {
            throw new IllegalOperationException("Post is already deleted or restored");
        }
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
        PostImage postImage = imageRepository.findByPostId(id);
        if (postImage != null) {
            imageRepository.delete(postImage);
        }
        postRepository.delete(post);
        return post;
    }

    public Post getPostByIdForExpertModerAdmin(long id, String token) throws ResourceNotFoundException, IllegalOperationException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id = " + id + " not found"));
        User user = userService.getUserByToken(token);
        List<Role> roles = user.getRoles();
        Role roleAdministrator = roleRepository.findByName("ROLE_ADMINISTRATOR");
        Role roleModerator = roleRepository.findByName("ROLE_MODERATOR");
        Role roleExpert = roleRepository.findByName("ROLE_EXPERT");
        if (post.isDeleted()) {
            if (roles.contains(roleExpert) &&
                    !roles.contains(roleAdministrator) &&
                    !roles.contains(roleModerator)) {
                if (post.getAuthor() == user.getId()) {
                    return post;
                }
                throw new IllegalOperationException("User haven't access to this post");
            }
        }
        return post;
    }

    public void ratePost(long postId, Mark mark) throws ResourceNotFoundException, IllegalOperationException {
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
        postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id = " + postId + " not found"));
        return markRepository.findByPostIdAndUserId(postId, userId);
    }

}
