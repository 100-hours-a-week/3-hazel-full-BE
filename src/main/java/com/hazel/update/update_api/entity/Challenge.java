package com.hazel.update.update_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "challenges")
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ChallengeCategory category;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Challenge() {
    }

    public Challenge(String title, String description, ChallengeCategory category, LocalDate endDate, User creator) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.endDate = endDate;
        this.creator = creator;
    }

    public void updateChallenge(String title, String description, ChallengeCategory category, LocalDate endDate) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.endDate = endDate;
    }

    public boolean isCreator(Long userId) {
        return userId.equals(this.creator.getId());
        // TODO: return Objects.equals(this.creator.getId(), userId); 랑 비교해서 공부
    }
}
