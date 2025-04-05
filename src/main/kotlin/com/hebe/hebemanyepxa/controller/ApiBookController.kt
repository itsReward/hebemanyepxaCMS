package com.hebe.hebemanyepxa.controller

import com.hebe.hebemanyepxa.model.Book
import com.hebe.hebemanyepxa.service.BookService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/public/books")
class ApiBookController(private val bookService: BookService) {

    @GetMapping
    fun getAll(
        @PageableDefault(size = 10, sort = ["title"], direction = Sort.Direction.ASC) pageable: Pageable
    ): ResponseEntity<Page<Book>> {
        val books = bookService.findAll(pageable)
        return ResponseEntity.ok(books)
    }

    @GetMapping("/{slug}")
    fun getBySlug(@PathVariable slug: String): ResponseEntity<Book> {
        val book = bookService.findBySlug(slug)
        return if (book.isPresent) {
            ResponseEntity.ok(book.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/featured")
    fun getFeatured(): ResponseEntity<List<Book>> {
        val featured = bookService.findFeatured()
        return ResponseEntity.ok(featured)
    }

    @GetMapping("/by-author")
    fun getByAuthor(
        @RequestParam author: String,
        @PageableDefault(size = 10) pageable: Pageable
    ): ResponseEntity<Page<Book>> {
        val books = bookService.findByAuthor(author, pageable)
        return ResponseEntity.ok(books)
    }
}