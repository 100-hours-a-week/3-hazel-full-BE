package com.hazel.update.update_api.controller;

import com.hazel.update.update_api.common.response.ApiResponse;
import com.hazel.update.update_api.config.jwt.UserPrincipal;
import com.hazel.update.update_api.dto.challenge.MyChallengeResponse;
import com.hazel.update.update_api.dto.post.PostDetailResponse;
import com.hazel.update.update_api.dto.post.PostRequest;
import com.hazel.update.update_api.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDetailResponse>> getPostDetail(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long postId) {
        PostDetailResponse response = postService.getPostDetail(userPrincipal.getId(), postId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> updatePost(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long postId, @Valid @RequestBody PostRequest request) {
        postService.updatePost(userPrincipal.getId(), postId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long postId) {
        postService.deletePost(userPrincipal.getId(), postId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<ApiResponse<Boolean>> toggleLike(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long postId) {
        boolean result = postService.toggleLike(userPrincipal.getId(), postId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }






}
