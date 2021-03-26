package com.imyourbuddy.petwebapp.repository;

import com.imyourbuddy.petwebapp.model.Post;
import com.imyourbuddy.petwebapp.model.projection.PostProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository interface for {@link Post} class
 */

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "SELECT p.id, p.title, p.description, p.text, u.first_name || ' ' || u.last_name as author, " +
            "p.created_date as \"createdDate\", p.deleted FROM post p INNER JOIN pet_expert pe ON p.author = pe.id\n" +
            "INNER JOIN public.user u ON pe.user_id = u.id\n" +
            "ORDER BY p.created_date DESC", nativeQuery = true)
    List<PostProjection> findAllInOrderByDate();

    @Query(value = "SELECT * FROM post p WHERE p.author =:author ORDER BY p.deleted DESC, p.created_date DESC", nativeQuery = true)
    List<Post> findAllByAuthor(@Param(value = "author") long author);


    @Query(value = "SELECT * FROM post p ORDER BY p.deleted DESC, p.created_date DESC", nativeQuery = true)
    List<Post> findAllForModer();

    @Modifying
    @Transactional
    @Query(value = "UPDATE post p SET deleted = true WHERE p.id =:id", nativeQuery = true)
    void delete(@Param("id") long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE post p SET deleted = false WHERE p.id =:id", nativeQuery = true)
    void restore(@Param("id") long id);

}
