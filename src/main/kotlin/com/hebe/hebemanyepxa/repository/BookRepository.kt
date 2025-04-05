package com.hebe.hebemanyepxa.repository

import com.hebe.hebemanyepxa.model.Book
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface BookRepository : JpaRepository<Book, Long> {
    fun findBySlug(slug: String): Optional<Book>
    fun findByIsFeaturedTrue(): List<Book>
    fun findByAuthorContainingIgnoreCase(author: String, pageable: Pageable): Page<Book>
    fun existsBySlug(slug: String): Boolean
}