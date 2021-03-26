package com.imyourbuddy.petwebapp.repository;

import com.imyourbuddy.petwebapp.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link Chat} class
 */

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query(value = "SELECT * FROM chat c WHERE " +
            "(c.user_id =:userId AND c.expert_id =:expertId) OR (c.user_id =:expertId AND c.expert_id =:userId)",
            nativeQuery = true)
    Chat findChatByUserIdAndAndExpertId(@Param(value = "userId") long userId,@Param(value = "expertId") long expertId);

    @Query(value = "SELECT * FROM chat c WHERE c.user_id =:id OR c.expert_id =:id", nativeQuery = true)
    List<Chat> findByUser(@Param(value = "id") long id);
}
