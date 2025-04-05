package com.hebe.hebemanyepxa.model

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.OffsetDateTime

@Entity
@Table(name = "images")
data class ApparelImage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apparel_id")
    val apparel: Apparel,

    @Column(name = "file_name", nullable = false)
    val fileName: String,

    @Column(name = "file_path", nullable = false)
    val filePath: String,

    @Column(name = "is_primary")
    val isPrimary: Boolean = false,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now()
)