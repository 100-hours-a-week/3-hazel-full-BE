package com.hazel.update.update_api.dto.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequest {

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private String imageUrl;
}
