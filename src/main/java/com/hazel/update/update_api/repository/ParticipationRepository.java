package com.hazel.update.update_api.repository;

import com.hazel.update.update_api.entity.Challenge;
import com.hazel.update.update_api.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    // TODO: v2에서 프론트/백 동시 중복 방지 처리 추가 예정

    int countByChallenge(Challenge challenge);

    List<Participation> findByUserId(Long userId);

    boolean existsByUserIdAndChallengeId(Long userId, Long challengeId);

    Optional<Participation> findByUserIdAndChallengeId(Long userId, Long challengeId);

    // userId가 참여중인 챌린지 목록
    @Query("select p.challenge.id from Participation p where p.user.id = :userId")
    List<Long> findChallengeIdsByUserId(Long userId);
}
