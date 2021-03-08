package com.imyourbuddy.petwebapp.repository;

import com.imyourbuddy.petwebapp.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link Message} class
 */

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
