package com.imyourbuddy.petwebapp.repository;

import com.imyourbuddy.petwebapp.model.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Repository interface for {@link PostImage} class.
 */

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    PostImage findByPostId(long postId);

}
