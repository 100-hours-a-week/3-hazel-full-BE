package com.hazel.update.update_api.service;

import com.hazel.update.update_api.common.exception.ApiException;
import com.hazel.update.update_api.common.exception.ErrorCode;
import com.hazel.update.update_api.repository.ParticipationRepository;
import com.hazel.update.update_api.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class ParticipationService {

    private final ParticipationRepository participationRepository;
    private final PostRepository postRepository;

    public ParticipationService(ParticipationRepository participationRepository, PostRepository postRepository) {
        this.participationRepository = participationRepository;
        this.postRepository = postRepository;
    }

    public void ensureParticipation(Long userId, Long challengeId) {
        if(!participationRepository.existsByUserIdAndChallengeId(userId, challengeId))
            throw new ApiException(ErrorCode.NOT_PARTICIPATING_CHALLENGE);
    }

}
