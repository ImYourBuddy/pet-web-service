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

    @Query(value = "SELECT * FROM message WHERE chat_id =:chatId ORDER BY timestamp", nativeQuery = true)
    List<Message> findAllByChatId(@Param("chatId") long chatId);

    List<Message> findAllBySender(long sender);

    @Query(value = "SELECT * FROM message WHERE chat_id =:chatId AND status = 'RECEIVED' AND sender !=:user",
            nativeQuery = true)
    List<Message> findAllReceived(@Param("chatId") long chatId, @Param("user") long user);

    @Modifying
    @Transactional
    @Query(value = "UPDATE public.message SET status = 'DELIVERED' WHERE chat_id =:chatId AND status = 'RECEIVED'",
            nativeQuery = true)
    void markAsDelivered(@Param("chatId") long chatId);
}
