package com.hazel.update.update_api.controller;

import com.hazel.update.update_api.common.response.ApiResponse;
import com.hazel.update.update_api.config.jwt.UserPrincipal;
import com.hazel.update.update_api.dto.challenge.*;
import com.hazel.update.update_api.dto.post.PostRequest;
import com.hazel.update.update_api.dto.post.PostSummaryResponse;
import com.hazel.update.update_api.service.ChallengeService;
import com.hazel.update.update_api.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;
    private final PostService postService;

    public ChallengeController(ChallengeService challengeService, PostService postService) {
        this.challengeService = challengeService;
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ChallengeSummaryResponse>>> getChallenges(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<ChallengeSummaryResponse> challengeSummaryResponses = challengeService.getChallenges(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(challengeSummaryResponses));
    }

    @GetMapping("/{challengeId}")
    public ResponseEntity<ApiResponse<ChallengeDetailResponse>> getChallengeDetail(@PathVariable Long challengeId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        ChallengeDetailResponse response = challengeService.getChallengeDetail(userPrincipal.getId(), challengeId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{challengeId}/preview")
    public ResponseEntity<ApiResponse<ChallengePreviewResponse>> getChallengePreview(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long challengeId) {
        ChallengePreviewResponse response = challengeService.getChallengePreview(userPrincipal.getId(), challengeId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/recommended")
    public ResponseEntity<ApiResponse<List<ChallengeSummaryResponse>>> getRecommendedChallenges(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam int size) {
        List<ChallengeSummaryResponse> responses = challengeService.getRecommendedChallenges(userPrincipal.getId(), size);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createChallenge(@AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody ChallengeRequest request) {
        Long challengeId = challengeService.createChallenge(userPrincipal.getId(), request);
        return ResponseEntity.ok(ApiResponse.success(challengeId));
    }

    @PatchMapping("/{challengeId}")
    public ResponseEntity<ApiResponse<Void>> updateChallenge(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long challengeId, @Valid @RequestBody ChallengeRequest request) {
        challengeService.updateChallenge(userPrincipal.getId(), challengeId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{challengeId}/participations")
    public ResponseEntity<ApiResponse<Long>> joinChallenge(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long challengeId) {
         Long resultChallengeId = challengeService.joinChallenge(userPrincipal.getId(), challengeId);
         return ResponseEntity.ok(ApiResponse.success(resultChallengeId));
    }

    @DeleteMapping("/{challengeId}/participations")
    public ResponseEntity<ApiResponse<Void>> leaveChallenge(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long challengeId) {
        challengeService.leaveChallenge(userPrincipal.getId(), challengeId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/{challengeId}/posts")
    public ResponseEntity<ApiResponse<List<PostSummaryResponse>>> getChallengePosts(@PathVariable Long challengeId) {
        List<PostSummaryResponse> posts = postService.getChallengePosts(challengeId);
        return ResponseEntity.ok(ApiResponse.success(posts));
    }

    @PostMapping("/{challengeId}/posts")
    public ResponseEntity<ApiResponse<Long>> createPost(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long challengeId, @Valid @RequestBody PostRequest request) {
        Long postId = postService.createPost(userPrincipal.getId(), challengeId, request);
        return ResponseEntity.ok(ApiResponse.success(postId));
    }


}

