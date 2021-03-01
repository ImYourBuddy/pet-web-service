package com.imyourbuddy.petwebapp.repository;

import com.imyourbuddy.petwebapp.model.Chat;
import com.imyourbuddy.petwebapp.model.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Chat} class
 */

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
}
