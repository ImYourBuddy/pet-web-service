package com.imyourbuddy.petwebapp.repository;

import com.imyourbuddy.petwebapp.model.PetExpert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository interface for {@link PetExpert} class
 */

@Repository
public interface PetExpertRepository extends JpaRepository<PetExpert, Long> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE public.pet_expert  SET confirmed = true WHERE id =:id", nativeQuery = true)
    void confirmExpert(@Param("id") long id);
}
