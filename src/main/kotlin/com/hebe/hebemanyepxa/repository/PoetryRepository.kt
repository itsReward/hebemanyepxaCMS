package com.hebe.hebemanyepxa.repository

import com.hebe.hebemanyepxa.model.Poetry
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.Optional

@Repository
interface PoetryRepository : JpaRepository<Poetry, Long> {
    fun findBySlug(slug: String): Optional<Poetry>
    fun findByIsFeaturedTrue(): List<Poetry>
    fun findByIsPublishedTrueOrderByPublishDateDesc(pageable: Pageable): Page<Poetry>
    fun findByPublishDateBetweenAndIsPublishedTrue(startDate: LocalDate, endDate: LocalDate, pageable: Pageable): Page<Poetry>
    fun existsBySlug(slug: String): Boolean
}