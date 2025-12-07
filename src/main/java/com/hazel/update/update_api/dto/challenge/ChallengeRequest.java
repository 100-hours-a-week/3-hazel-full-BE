package com.hazel.update.update_api.dto.challenge;

import com.hazel.update.update_api.entity.ChallengeCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ChallengeRequest {
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    private String description;
    private ChallengeCategory category;
    private LocalDate endDate;
}
