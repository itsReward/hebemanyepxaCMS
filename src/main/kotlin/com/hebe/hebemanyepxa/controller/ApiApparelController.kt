package com.hebe.hebemanyepxa.controller

import com.hebe.hebemanyepxa.model.Apparel
import com.hebe.hebemanyepxa.service.ApparelService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/public/apparel")
class ApiApparelController(private val apparelService: ApparelService) {

    @GetMapping
    fun getAllAvailable(
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<Page<Apparel>> {
        val apparel = apparelService.findAvailable(pageable)
        return ResponseEntity.ok(apparel)
    }

    @GetMapping("/{slug}")
    fun getBySlug(@PathVariable slug: String): ResponseEntity<Apparel> {
        val apparel = apparelService.findBySlug(slug)
        return if (apparel.isPresent) {
            ResponseEntity.ok(apparel.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/featured")
    fun getFeatured(): ResponseEntity<List<Apparel>> {
        val featured = apparelService.findFeatured()
        return ResponseEntity.ok(featured)
    }
}