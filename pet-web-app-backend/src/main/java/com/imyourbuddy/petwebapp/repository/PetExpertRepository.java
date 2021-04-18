package com.imyourbuddy.petwebapp.repository;

import com.imyourbuddy.petwebapp.model.PetExpert;
import com.imyourbuddy.petwebapp.model.projection.PetExpertQueryResult;
import com.imyourbuddy.petwebapp.model.projection.PetExpertRequestQueryResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link PetExpert} class
 */

@Repository
public interface PetExpertRepository extends JpaRepository<PetExpert, Long> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE public.pet_expert  SET confirmed = true WHERE id =:id", nativeQuery = true)
    void confirmExpert(@Param("id") long id);

    @Query(value = "SELECT pe.user_id as userId, u.first_name || ' ' || u.last_name as name, pe.qualification, " +
            "pe.online_help as help, pe.reputation as reputation FROM pet_expert pe INNER JOIN public.user u " +
            "ON pe.user_id = u.id WHERE pe.online_help = true AND pe.confirmed = true",
            nativeQuery = true)
    public List<PetExpertQueryResult> findAllConfirmedExperts();

    @Modifying
    @Transactional
    @Query(value = "UPDATE public.pet_expert SET reputation = reputation + 1 WHERE id =:id", nativeQuery = true)
    void increaseReputation(@Param("id") long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE public.pet_expert SET reputation = reputation - 1 WHERE id =:id", nativeQuery = true)
    void decreaseReputation(@Param("id") long id);

    Optional<PetExpert> findByUserId(long userId);

    @Query(value = "select pe.id, pe.user_id as user, u.username, u.first_name || ' '|| u.last_name as name, " +
            "pe.qualification, pe.online_help as help\n" +
            "from public.user u INNER JOIN pet_expert pe ON u.id = pe.user_id\n" +
            "where pe.confirmed = false", nativeQuery = true)
    List<PetExpertRequestQueryResult> getExpertRequest();
}
