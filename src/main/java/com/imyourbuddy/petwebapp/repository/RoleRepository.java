package com.imyourbuddy.petwebapp.repository;

import com.imyourbuddy.petwebapp.model.Post;
import com.imyourbuddy.petwebapp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Role} class.
 */

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByName(String name);
}
