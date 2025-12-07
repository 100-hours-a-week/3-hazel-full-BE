package com.hazel.update.update_api.dto.challenge;

import com.hazel.update.update_api.entity.ChallengeCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeDetailResponse {
    private Long id;
    private String title;
    private String description;
    private ChallengeCategory category;
    private LocalDate endDate;
    private int participantCount;
    private boolean hasUpdatedToday;
    private boolean isCreator;
}

