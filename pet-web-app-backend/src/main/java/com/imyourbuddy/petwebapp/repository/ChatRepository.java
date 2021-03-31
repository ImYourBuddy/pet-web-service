package com.imyourbuddy.petwebapp.repository;

import com.imyourbuddy.petwebapp.model.Chat;
import com.imyourbuddy.petwebapp.model.projection.ChatProjection;
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

    @Query(value = "select c.id, c.user_id as user, u_1.first_name || ' ' || u_1.last_name as userName,\n" +
            "c.expert_id as expert, u_2.first_name || ' ' || u_2.last_name as expertName\n" +
            "from chat c \n" +
            "inner join public.user u_1 on c.user_id = u_1.id\n" +
            "inner join public.user u_2 on c.expert_id = u_2.id WHERE c.user_id =:id OR c.expert_id =:id", nativeQuery = true)
    List<ChatProjection> findByUser(@Param(value = "id") long id);
}
