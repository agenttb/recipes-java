package com.bintian.learn.i18nsolution.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "translation",
        uniqueConstraints = @UniqueConstraint(columnNames = {"message_key", "locale"}),
        indexes = {
                @Index(name = "idx_locale", columnList = "locale"),
                @Index(name = "idx_category", columnList = "category")
        })
public class Translation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message_key", nullable = false, length = 255)
    private String messageKey;

    @Column(name = "locale", nullable = false, length = 10)
    private String locale;

    @Column(name = "message_value", nullable = false, columnDefinition = "TEXT")
    private String messageValue;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
