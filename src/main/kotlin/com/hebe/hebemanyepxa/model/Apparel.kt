package com.hebe.hebemanyepxa.model

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.awt.Image
import java.math.BigDecimal
import java.time.OffsetDateTime

@Entity
@Table(name = "apparel")
data class Apparel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false, unique = true)
    val slug: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val description: String,

    @Column(nullable = false, precision = 10, scale = 2)
    val price: BigDecimal,

    @Column(name = "is_featured")
    val isFeatured: Boolean = false,

    @Column(name = "is_available")
    val isAvailable: Boolean = true,

    @OneToMany(mappedBy = "apparel", cascade = [CascadeType.ALL], orphanRemoval = true)
    val images: MutableList<ApparelImage> = mutableListOf(),

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    val updatedAt: OffsetDateTime = OffsetDateTime.now()
)