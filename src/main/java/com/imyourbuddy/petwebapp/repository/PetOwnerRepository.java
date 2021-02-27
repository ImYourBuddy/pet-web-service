package com.imyourbuddy.petwebapp.repository;

import com.imyourbuddy.petwebapp.model.PetOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link PetOwner} class
 */

@Repository
public interface PetOwnerRepository extends JpaRepository<PetOwner, Long> {
}
