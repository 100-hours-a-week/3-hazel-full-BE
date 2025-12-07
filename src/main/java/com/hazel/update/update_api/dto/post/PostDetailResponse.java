package com.hazel.update.update_api.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailResponse {
    private Long id;
    private String challengeTitle;
    private String writerNickname;
    private LocalDateTime createdAt;
    private int viewCount;
    private String content;
    private String imageUrl;
    private int likeCount;
    private boolean liked;
}