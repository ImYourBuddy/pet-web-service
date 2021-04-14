package com.imyourbuddy.petwebapp.repository;


import com.imyourbuddy.petwebapp.model.Ban;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Ban} class
 */

@Repository
public interface BanRepository extends JpaRepository<Ban, Long> {
    Ban findByUserId(long userId);
}
