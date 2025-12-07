package com.hazel.update.update_api.service;

import com.hazel.update.update_api.common.exception.ApiException;
import com.hazel.update.update_api.common.exception.ErrorCode;
import com.hazel.update.update_api.dto.post.PostDetailResponse;
import com.hazel.update.update_api.dto.post.PostRequest;
import com.hazel.update.update_api.dto.post.PostSummaryResponse;
import com.hazel.update.update_api.entity.Participation;
import com.hazel.update.update_api.entity.Post;
import com.hazel.update.update_api.entity.PostLike;
import com.hazel.update.update_api.entity.User;
import com.hazel.update.update_api.repository.ParticipationRepository;
import com.hazel.update.update_api.repository.PostLikeRepository;
import com.hazel.update.update_api.repository.PostRepository;
import com.hazel.update.update_api.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final ParticipationRepository participationRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final ParticipationService participationService;

    public PostService(PostRepository postRepository, ParticipationRepository participationRepository, PostLikeRepository postLikeRepository, UserRepository userRepository, ParticipationService participationService) {
        this.postRepository = postRepository;
        this.participationRepository = participationRepository;
        this.postLikeRepository = postLikeRepository;
        this.userRepository = userRepository;
        this.participationService = participationService;
    }

    @Transactional
    public Long createPost(Long currentUserId, Long challengeId, PostRequest request) {
        Participation participation = participationRepository.findByUserIdAndChallengeId(currentUserId, challengeId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_PARTICIPATING_CHALLENGE));

        // TODO: v2에서 실제 이미지 업로드 처리 후 URL 주입
        Post post = new Post(participation, request.getContent(), request.getImageUrl());
        postRepository.save(post);

        return post.getId();
    }

    @Transactional
    public PostDetailResponse getPostDetail(Long currentUserId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.POST_NOT_FOUND));

        post.increaseViewCount();

        Participation participation = post.getParticipation();

        return new PostDetailResponse(
                post.getId(),
                participation.getChallenge().getTitle(),
                participation.getUser().getNickname(),
                post.getCreatedAt(),
                post.getViewCount(),
                post.getContent(),
                post.getImageUrl(),
                postLikeRepository.countByPostId(postId),
                postLikeRepository.existsByUserIdAndPostId(currentUserId, postId)
        );
    }

    @Transactional(readOnly = true)
    public List<PostSummaryResponse> getChallengePosts(Long challengeId) {
        List<Post> posts = postRepository.findPostsWithUserByChallengeId(challengeId);

        return posts.stream()
                .map(post -> new PostSummaryResponse(
                        post.getId(),
                        post.getParticipation().getUser().getNickname(),
                        post.getContent(),
                        post.getCreatedAt()
                ))
                .toList();
    }


    @Transactional(readOnly = true)
    public List<PostSummaryResponse> getMyChallengePosts(Long currentUserId, Long challengeId) {
        Participation participation = participationRepository.findByUserIdAndChallengeId(currentUserId, challengeId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_PARTICIPATING_CHALLENGE));

        String writerNickname = participation.getUser().getNickname();

        List<Post> posts = postRepository.findByParticipationOrderByCreatedAtDesc(participation);

        List<PostSummaryResponse> postsSummary = new ArrayList<>();
        for (Post post : posts) {
            postsSummary.add(new PostSummaryResponse(
                    post.getId(),
                    writerNickname,
                    post.getContent(),
                    post.getCreatedAt()
            ));
        }
        return postsSummary;
    }


    @Transactional
    public void updatePost(Long currentUserId, Long postId, PostRequest request) {
        //TODO: N+1 문제 해결
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.POST_NOT_FOUND));

        Participation participation = post.getParticipation();
        Long challengeId = participation.getChallenge().getId();
        participationService.ensureParticipation(currentUserId, challengeId);

        Long writerId = participation.getUser().getId();
        if (!writerId.equals(currentUserId)) {
            throw new ApiException(ErrorCode.POST_FORBIDDEN);
        }

        post.update(request.getContent(), request.getImageUrl());
    }

    @Transactional
    public void deletePost(Long currentUserId, Long postId) {
        //TODO: N+1 문제 해결
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.POST_NOT_FOUND));

        Participation participation = post.getParticipation();
        Long challengeId = participation.getChallenge().getId();
        participationService.ensureParticipation(currentUserId, challengeId);

        Long writerId = participation.getUser().getId();
        if (!writerId.equals(currentUserId)) {
            throw new ApiException(ErrorCode.POST_FORBIDDEN);
        }

        //TODO: soft delete 구현시 수정
        postRepository.delete(post);
    }

    @Transactional
    public boolean toggleLike(Long currentUserId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(ErrorCode.POST_NOT_FOUND));

        Participation participation = post.getParticipation();
        Long challengeId = participation.getChallenge().getId();
        participationService.ensureParticipation(currentUserId, challengeId);

        Optional<PostLike> existing = postLikeRepository.findByUserIdAndPostId(currentUserId, postId);

        if (existing.isPresent()) {
            postLikeRepository.delete(existing.get());
            return false;
        }

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        postLikeRepository.save(new PostLike(user, post));
        return true;
    }

}
