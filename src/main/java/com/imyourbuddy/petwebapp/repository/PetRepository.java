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

    public List<Pet> findByOwner(long ownerId);

    public Pet findById(long id);
}
