    package com.hazel.update.update_api.entity;

    import jakarta.persistence.*;
    import lombok.Getter;
    import org.springframework.data.annotation.CreatedDate;
    import org.springframework.data.jpa.domain.support.AuditingEntityListener;

    import java.time.LocalDateTime;

    @Entity
    @Table(name = "participations")
    @EntityListeners(AuditingEntityListener.class)
    @Getter
    public class Participation {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "challenge_id", nullable = false)
        private Challenge challenge;

        @CreatedDate
        @Column(name = "joined_at", nullable = false, updatable = false)
        private LocalDateTime joinedAt;

        protected Participation() {}
        public Participation(User user, Challenge challenge) {
            this.user = user;
            this.challenge = challenge;
        }
    }
