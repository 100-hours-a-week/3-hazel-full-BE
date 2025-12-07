package com.hazel.update.update_api.dto.challenge;

import com.hazel.update.update_api.entity.ChallengeCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChallengePreviewResponse {
    Long id;
    String title;
    String description;
    ChallengeCategory category;
    LocalDate endDate;
    int participantCount;
    boolean isParticipating;
}
