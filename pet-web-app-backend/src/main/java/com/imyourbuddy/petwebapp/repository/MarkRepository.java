package com.imyourbuddy.petwebapp.repository;


import com.imyourbuddy.petwebapp.model.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Mark} class
 */

@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {

    Mark findByPostIdAndUserId(long postId, long userId);

    List<Mark> findByPostId(long postId);

}
