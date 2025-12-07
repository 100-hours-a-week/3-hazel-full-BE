package com.hazel.update.update_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participation_id", nullable = false)
    private Participation participation;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "view_count", nullable = false)
    private int viewCount;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    @CreatedDate
    @Column(name = "created_at", nullable = false,  updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Post() {}
    public Post(Participation participation, String content, String imageUrl) {
        this.participation = participation;
        this.content = content;
        this.imageUrl = imageUrl;
        this.viewCount = 0;
        this.deleted = false;
    }

    public void update(String content, String imageUrl) {
        this.content = content;
        this.imageUrl = imageUrl;
    }
    public void increaseViewCount() {
        this.viewCount++;
    }
    public void delete() {
        this.deleted = true;
    }
}
