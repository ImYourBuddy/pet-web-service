package com.imyourbuddy.petwebapp.repository;

import com.imyourbuddy.petwebapp.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Post} class
 */

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
