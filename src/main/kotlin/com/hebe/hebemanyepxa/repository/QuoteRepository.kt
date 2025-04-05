package com.hebe.hebemanyepxa.repository

import com.hebe.hebemanyepxa.model.Quote
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QuoteRepository : JpaRepository<Quote, Long> {
    fun findByIsFeaturedTrue(): List<Quote>
    fun findByAuthorContainingIgnoreCase(author: String, pageable: Pageable): Page<Quote>
    fun findByCategoryContainingIgnoreCase(category: String, pageable: Pageable): Page<Quote>
}