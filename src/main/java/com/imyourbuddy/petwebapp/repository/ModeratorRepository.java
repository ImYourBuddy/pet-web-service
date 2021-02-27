package com.imyourbuddy.petwebapp.repository;


import com.imyourbuddy.petwebapp.model.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Moderator} class
 */

@Repository
public interface ModeratorRepository extends JpaRepository<Moderator, Integer> {
}
