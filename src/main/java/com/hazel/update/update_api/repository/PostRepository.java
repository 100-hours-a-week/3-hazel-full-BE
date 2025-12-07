package com.hazel.update.update_api.repository;

import com.hazel.update.update_api.entity.Participation;
import com.hazel.update.update_api.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByParticipation_Challenge_IdOrderByCreatedAtDesc(Long challengeId);

    List<Post> findByParticipationOrderByCreatedAtDesc(Participation participation);

    @Query("""
                SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END
                FROM Post p
                WHERE p.participation.user.id = :userId
                  AND p.participation.challenge.id = :challengeId
                  AND DATE(p.createdAt) = :today
            """)
    boolean existsTodayPost(Long userId, Long challengeId, LocalDate today);

    @Query("""
    select p from Post p
    join fetch p.participation part
    join fetch part.user
    where part.challenge.id = :challengeId
    order by p.createdAt desc
    """)
    List<Post> findPostsWithUserByChallengeId(Long challengeId);

}