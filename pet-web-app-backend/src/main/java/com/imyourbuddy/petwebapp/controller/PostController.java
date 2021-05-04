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
    public List<PostQueryResult> getAllExceptDeleted() {
        return service.getAllExceptDeleted();
    }

    @GetMapping("/{id}")
    public Post getPostByIdExceptDeleted(@PathVariable(name = "id") long id) throws ResourceNotFoundException, IllegalOperationException {
        return service.getPostByIdExceptDeleted(id);
    }

    @GetMapping("/{id}/image")
    public PostImage getImageByPostId(@PathVariable(name = "id") long id) throws ResourceNotFoundException {
        return service.getPostImageByPostId(id);

    }

    @GetMapping("/author/{author}")
    @PreAuthorize("hasRole('EXPERT') or hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public List<Post> getPostsByAuthor(@PathVariable(name = "author") long author) throws ResourceNotFoundException {
        return service.getPostsByAuthor(author);
    }

    @PostMapping()
    @PreAuthorize("hasRole('EXPERT') or hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public Post addNewPost(@RequestPart(value = "file", required = false) MultipartFile image,
                           @RequestPart(value = "post") @Valid Post post) throws ResourceNotFoundException {
        return service.save(post, image);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EXPERT') or hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public Post editPost(@PathVariable(name = "id") long id,
                         @RequestPart(value = "post") @Valid Post post,
                         @RequestPart(value = "file", required = false) MultipartFile image) throws ResourceNotFoundException {
        return service.edit(id, post, image);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('EXPERT') or hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public Post removeFromPublicAccess(@PathVariable(name = "id") long id,
                                       @RequestBody DeletePostRequest deleteRequest) throws ResourceNotFoundException, IllegalOperationException {
        return service.removeFromPublicAccess(id, deleteRequest.isDelete());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public List<Post> getAll() {
        return service.getAll();
    }

    @GetMapping("/all/{id}")
    @PreAuthorize("hasRole('EXPERT') or hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public Post getPostById(@PathVariable(name = "id") long id, HttpServletRequest request) throws ResourceNotFoundException, IllegalOperationException {
        String token = request.getHeader("Authorization");
        return service.getPostById(id, token.substring(7, token.length()));

    }

    @DeleteMapping("/all/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMINISTRATOR')")
    public Post deletePost(@PathVariable(name = "id") long id) throws ResourceNotFoundException {
        return service.deletePost(id);
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
                          @PathVariable(name = "userId") long userId) throws ResourceNotFoundException {
        return service.checkMark(postId, userId);
    }


}
