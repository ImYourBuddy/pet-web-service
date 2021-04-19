package com.imyourbuddy.petwebapp.repository;

import com.imyourbuddy.petwebapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

/**
 * Repository interface for {@link User} class/
 */

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
    @Query(value = "UPDATE public.user SET banned =:banned WHERE id =:id", nativeQuery = true)
    void banUserById(@Param("id") long id, @Param("banned") boolean banned);


}
