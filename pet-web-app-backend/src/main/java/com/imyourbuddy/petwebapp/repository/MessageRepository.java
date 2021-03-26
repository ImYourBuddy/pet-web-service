package com.imyourbuddy.petwebapp.repository;

import com.imyourbuddy.petwebapp.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository interface for {@link Message} class
 */

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByChatId(long chatId);

    List<Message> findAllBySender(long sender);
}
