package com.hazel.update.update_api.controller;

import com.hazel.update.update_api.common.response.ApiResponse;
import com.hazel.update.update_api.config.jwt.UserPrincipal;
import com.hazel.update.update_api.dto.challenge.MyChallengeResponse;
import com.hazel.update.update_api.dto.post.PostSummaryResponse;
import com.hazel.update.update_api.dto.user.ChangePasswordRequest;
import com.hazel.update.update_api.dto.user.UpdateNicknameRequest;
import com.hazel.update.update_api.dto.user.UserProfileResponse;
import com.hazel.update.update_api.service.AuthService;
import com.hazel.update.update_api.service.ChallengeService;
import com.hazel.update.update_api.service.PostService;
import com.hazel.update.update_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/me")
public class UserController {
    private final UserService userService;
    private final ChallengeService challengeService;
    private final PostService postService;

    public UserController(UserService userService, ChallengeService challengeService, PostService postService) {
        this.userService = userService;
        this.challengeService = challengeService;
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMyProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserProfileResponse profile = userService.getUserProfile(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<Void>> updateNickname(@Valid @RequestBody UpdateNicknameRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        userService.changeNickname(userPrincipal.getId(), request.getNickname());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        userService.changePassword(userPrincipal.getId(), changePasswordRequest);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        userService.deleteUser(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/challenges")
    public ResponseEntity<ApiResponse<List<MyChallengeResponse>>> getMyChallenges(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<MyChallengeResponse> responses = challengeService.getMyChallenges(userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/challenges/{challengeId}/posts")
    public ResponseEntity<ApiResponse<List<PostSummaryResponse>>> getMyChallengePosts(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long challengeId) {
        List<PostSummaryResponse> responses = postService.getMyChallengePosts(userPrincipal.getId(), challengeId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

}
