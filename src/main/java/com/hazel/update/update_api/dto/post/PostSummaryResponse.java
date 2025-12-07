package com.hazel.update.update_api.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostSummaryResponse {
    private Long id;
    private String writerNickname;
    private String content;
    private LocalDateTime createdAt;
}
