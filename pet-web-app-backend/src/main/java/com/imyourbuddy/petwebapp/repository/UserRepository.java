package com.imyourbuddy.petwebapp.repository;

import com.imyourbuddy.petwebapp.model.User;
import com.imyourbuddy.petwebapp.model.projection.PetExpertRequestProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    @Modifying
    @Transactional
    @Query(value = "UPDATE public.user u SET deleted = true WHERE u.id =:id", nativeQuery = true)
    void deleteUserById(@Param("id") long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE public.user u SET u.banned =:banned WHERE u.id =:id", nativeQuery = true)
    void banUserById(@Param("id") long id, @Param("banned") boolean banned);

    @Query(value = "select pe.id, pe.user_id as user, u.username, u.first_name || ' '|| u.last_name as name, " +
            "pe.qualification, pe.online_help as help\n" +
            "from public.user u INNER JOIN pet_expert pe ON u.id = pe.user_id\n" +
            "where pe.confirmed = false", nativeQuery = true)
    List<PetExpertRequestProjection> getExpertRequest();

}
