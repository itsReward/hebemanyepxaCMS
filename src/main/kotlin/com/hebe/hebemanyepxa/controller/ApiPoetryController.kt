package com.hebe.hebemanyepxa.controller

import com.hebe.hebemanyepxa.model.Poetry
import com.hebe.hebemanyepxa.service.PoetryService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/public/poetry")
class ApiPoetryController(private val poetryService: PoetryService) {

    @GetMapping
    fun getAllPublished(
        @PageableDefault(size = 10, sort = ["publishDate"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<Page<Poetry>> {
        val poetry = poetryService.findPublished(pageable)
        return ResponseEntity.ok(poetry)
    }

    @GetMapping("/{slug}")
    fun getBySlug(@PathVariable slug: String): ResponseEntity<Poetry> {
        val poetry = poetryService.findBySlug(slug)
        return if (poetry.isPresent) {
            ResponseEntity.ok(poetry.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/featured")
    fun getFeatured(): ResponseEntity<List<Poetry>> {
        val featured = poetryService.findFeatured()
        return ResponseEntity.ok(featured)
    }

    @GetMapping("/by-date")
    fun getByDateRange(
        @RequestParam startDate: LocalDate,
        @RequestParam endDate: LocalDate,
        @PageableDefault(size = 10) pageable: Pageable
    ): ResponseEntity<Page<Poetry>> {
        val poetry = poetryService.findByDateRange(startDate, endDate, pageable)
        return ResponseEntity.ok(poetry)
    }
}