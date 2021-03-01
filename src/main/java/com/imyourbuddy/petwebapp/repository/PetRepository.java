package com.imyourbuddy.petwebapp.repository;

import com.imyourbuddy.petwebapp.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Pet} class
 */

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
}
