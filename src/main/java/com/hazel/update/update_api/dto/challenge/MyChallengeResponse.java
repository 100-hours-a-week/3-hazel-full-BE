package com.hazel.update.update_api.dto.challenge;

import com.hazel.update.update_api.entity.ChallengeCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyChallengeResponse {
    private Long challengeId;
    private Long participationId;
    private String title;
    private ChallengeCategory category;
    private LocalDate endDate;
    private boolean todayUpdated;
}
