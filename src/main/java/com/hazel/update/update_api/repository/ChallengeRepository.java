package com.hazel.update.update_api.repository;

import com.hazel.update.update_api.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    @Query("""
        select c.id as id,
               c.title as title,
               c.category as category,
               c.endDate as endDate,
               count(p.id) as participantCount
        from Challenge c
        left join Participation p on p.challenge = c
        group by c.id, c.title, c.category, c.endDate, c.createdAt
        order by c.createdAt desc
    """)
    List<ChallengeSummaryProjection> findAllWithParticipantCountOrderByCreatedAtDesc();

}
