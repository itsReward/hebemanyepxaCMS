package com.hebe.hebemanyepxa.controller

import com.hebe.hebemanyepxa.model.Quote
import com.hebe.hebemanyepxa.service.QuoteService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/public/quotes")
class ApiQuoteController(private val quoteService: QuoteService) {

    @GetMapping
    fun getAll(
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<Page<Quote>> {
        val quotes = quoteService.findAll(pageable)
        return ResponseEntity.ok(quotes)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Quote> {
        val quote = quoteService.findById(id)
        return if (quote.isPresent) {
            ResponseEntity.ok(quote.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/featured")
    fun getFeatured(): ResponseEntity<List<Quote>> {
        val featured = quoteService.findFeatured()
        return ResponseEntity.ok(featured)
    }

    @GetMapping("/by-author")
    fun getByAuthor(
        @RequestParam author: String,
        @PageableDefault(size = 10) pageable: Pageable
    ): ResponseEntity<Page<Quote>> {
        val quotes = quoteService.findByAuthor(author, pageable)
        return ResponseEntity.ok(quotes)
    }

    @GetMapping("/by-category")
    fun getByCategory(
        @RequestParam category: String,
        @PageableDefault(size = 10) pageable: Pageable
    ): ResponseEntity<Page<Quote>> {
        val quotes = quoteService.findByCategory(category, pageable)
        return ResponseEntity.ok(quotes)
    }
}