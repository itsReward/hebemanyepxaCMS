package com.hebe.hebemanyepxa.repository

import com.hebe.hebemanyepxa.model.Apparel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ApparelRepository : JpaRepository<Apparel, Long> {
    fun findBySlug(slug: String): Optional<Apparel>
    fun findByIsFeaturedTrue(): List<Apparel>
    fun findByIsAvailableTrue(pageable: Pageable): Page<Apparel>
    fun existsBySlug(slug: String): Boolean
}