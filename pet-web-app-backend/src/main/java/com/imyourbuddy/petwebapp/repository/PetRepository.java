package com.imyourbuddy.petwebapp.repository;

import com.imyourbuddy.petwebapp.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Pet} class
 */

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByOwner(long ownerId);

    Pet findById(long id);
}
