package com.hazel.update.update_api.repository;

import com.hazel.update.update_api.entity.ChallengeCategory;
import java.time.LocalDate;

public interface ChallengeSummaryProjection {
    Long getId();
    String getTitle();
    ChallengeCategory getCategory();
    LocalDate getEndDate();
    long getParticipantCount();
}
