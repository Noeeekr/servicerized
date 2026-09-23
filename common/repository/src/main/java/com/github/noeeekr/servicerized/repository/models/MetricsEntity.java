package com.github.noeeekr.servicerized.repository.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class MetricsEntity {
    public static final class METADATA {
        public static final String DATABASE_COLUMN_NAME_CREATED_AT = "created_at";
        public static final String DATABASE_COLUMN_NAME_DELETED_AT = "deleted_at";
        public static final String DATABASE_COLUMN_NAME_UPDATED_AT = "updated_at";
    }

    @CreationTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ColumnDefault("NULL")
    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deletedAt;

    @UpdateTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
