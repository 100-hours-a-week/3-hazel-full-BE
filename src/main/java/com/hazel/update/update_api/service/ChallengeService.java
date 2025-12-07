package com.hazel.update.update_api.service;

import com.hazel.update.update_api.common.exception.ApiException;
import com.hazel.update.update_api.common.exception.ErrorCode;
import com.hazel.update.update_api.dto.challenge.*;
import com.hazel.update.update_api.entity.Challenge;
import com.hazel.update.update_api.entity.Participation;
import com.hazel.update.update_api.entity.User;
import com.hazel.update.update_api.repository.*;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import java.util.stream.Collectors;

@Service
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ParticipationService participationService;

    public ChallengeService(ChallengeRepository challengeRepository, ParticipationRepository participationRepository, UserRepository userRepository, PostRepository postRepository, ParticipationService participationService) {
        this.challengeRepository = challengeRepository;
        this.participationRepository = participationRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.participationService = participationService;
    }

    public List<ChallengeSummaryResponse> getChallenges(Long currentUserId){
        List<ChallengeSummaryProjection> projections = challengeRepository.findAllWithParticipantCountOrderByCreatedAtDesc();
        Set<Long> joinedIdSet = new HashSet<>(participationRepository.findChallengeIdsByUserId(currentUserId));

        return projections.stream()
                .map(p -> new ChallengeSummaryResponse(
                        p.getId(),
                        p.getTitle(),
                        p.getCategory(),
                        p.getEndDate(),
                        (int) p.getParticipantCount(),
                        joinedIdSet.contains(p.getId())
                ))
                .toList();
    }

    public List<ChallengeSummaryResponse> getRecommendedChallenges(Long currentUserId, int size) {
        List<ChallengeSummaryResponse> allChallenges = getChallenges(currentUserId);

        return allChallenges.stream()
                .sorted(Comparator.comparingInt(ChallengeSummaryResponse::getParticipantCount).reversed())
                .limit(size)
                .toList();
    }

    @Transactional(readOnly = true)
    public ChallengeDetailResponse getChallengeDetail(Long currentUserId, Long challengeId) {
        participationService.ensureParticipation(currentUserId, challengeId);

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ApiException(ErrorCode.CHALLENGE_NOT_FOUND));

        return new ChallengeDetailResponse(
                challenge.getId(),
                challenge.getTitle(),
                challenge.getDescription(),
                challenge.getCategory(),
                challenge.getEndDate(),
                participationRepository.countByChallenge(challenge),
                postRepository.existsTodayPost(currentUserId, challengeId, LocalDate.now()),
                challenge.getCreator().getId().equals(currentUserId)
        );
    }

    @Transactional(readOnly = true)
    public ChallengePreviewResponse getChallengePreview(Long currentUserId, Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ApiException(ErrorCode.CHALLENGE_NOT_FOUND));

        return new ChallengePreviewResponse(
                challenge.getId(),
                challenge.getTitle(),
                challenge.getDescription(),
                challenge.getCategory(),
                challenge.getEndDate(),
                participationRepository.countByChallenge(challenge),
                participationRepository.existsByUserIdAndChallengeId(currentUserId, challengeId)
        );
    }

    @Transactional
    public Long createChallenge(Long currentUserId, ChallengeRequest request) {
        User creator = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        Challenge challenge = new Challenge(
                request.getTitle(),
                request.getDescription(),
                request.getCategory(),
                request.getEndDate(),
                creator
        );

        challengeRepository.save(challenge);

        return challenge.getId();
    }

    @Transactional
    public void updateChallenge(Long currentUserId, Long challengeId, ChallengeRequest request) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ApiException(ErrorCode.CHALLENGE_NOT_FOUND));

        if (!challenge.isCreator(currentUserId)) {
            throw new ApiException(ErrorCode.CHALLENGE_UPDATE_FORBIDDEN);
        }

        challenge.updateChallenge(
                request.getTitle(),
                request.getDescription(),
                request.getCategory(),
                request.getEndDate()
        );
    }

    @Transactional(readOnly = true)
    public List<MyChallengeResponse> getMyChallenges(Long currentUserId) {

        List<Participation> participations = participationRepository.findByUserId(currentUserId);

        LocalDate today = LocalDate.now();
        LocalDateTime startOfToday = today.atStartOfDay();
        LocalDateTime endOfToday = today.plusDays(1).atStartOfDay();

        List<MyChallengeResponse> responses = new ArrayList<>();

        for (Participation participation : participations) {
            Challenge challenge = participation.getChallenge();
            responses.add(
                    new MyChallengeResponse(
                            challenge.getId(),
                            participation.getId(),
                            challenge.getTitle(),
                            challenge.getCategory(),
                            challenge.getEndDate(),
                            postRepository.existsTodayPost(currentUserId, challenge.getId(), LocalDate.now())
                    )
            );
        }

        return responses;
    }

    @Transactional
    public Long joinChallenge(Long currentUserId, Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new ApiException(ErrorCode.CHALLENGE_NOT_FOUND));
        if(participationRepository.existsByUserIdAndChallengeId(currentUserId, challengeId))
            throw new ApiException(ErrorCode.ALREADY_JOINED_CHALLENGE);
        User user = userRepository.findById(currentUserId).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        participationRepository.save(new Participation(user, challenge));
        return challenge.getId();
    }

    @Transactional
    public void leaveChallenge(Long currentUserId, Long challengeId) {

        Participation participation = participationRepository
                .findByUserIdAndChallengeId(currentUserId, challengeId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_PARTICIPATING_CHALLENGE));

        participationRepository.delete(participation);
    }



}
