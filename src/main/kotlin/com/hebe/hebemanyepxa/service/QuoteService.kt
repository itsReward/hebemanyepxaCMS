package com.hebe.hebemanyepxa.service

import com.hebe.hebemanyepxa.model.Quote
import com.hebe.hebemanyepxa.repository.QuoteRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
class QuoteService(private val quoteRepository: QuoteRepository) {

    fun findAll(pageable: Pageable): Page<Quote> {
        return quoteRepository.findAll(pageable)
    }

    fun findById(id: Long): Optional<Quote> {
        return quoteRepository.findById(id)
    }

    fun findFeatured(): List<Quote> {
        return quoteRepository.findByIsFeaturedTrue()
    }

    fun findByAuthor(author: String, pageable: Pageable): Page<Quote> {
        return quoteRepository.findByAuthorContainingIgnoreCase(author, pageable)
    }

    fun findByCategory(category: String, pageable: Pageable): Page<Quote> {
        return quoteRepository.findByCategoryContainingIgnoreCase(category, pageable)
    }

    @Transactional
    fun create(quote: Quote): Quote {
        return quoteRepository.save(quote)
    }

    @Transactional
    fun update(id: Long, quote: Quote): Optional<Quote> {
        return quoteRepository.findById(id).map { existingQuote ->
            val updatedQuote = quote.copy(
                id = existingQuote.id,
                createdAt = existingQuote.createdAt
            )
            quoteRepository.save(updatedQuote)
        }
    }

    @Transactional
    fun delete(id: Long) {
        quoteRepository.deleteById(id)
    }

    @Transactional
    fun count(): Long {
        return quoteRepository.count()
    }
}