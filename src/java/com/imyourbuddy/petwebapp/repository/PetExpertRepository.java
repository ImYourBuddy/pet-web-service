package com.imyourbuddy.petwebapp.repository;

import com.imyourbuddy.petwebapp.model.PetExpert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link PetExpert} class
 */

@Repository
public interface PetExpertRepository extends JpaRepository<PetExpert, Long> {
}
