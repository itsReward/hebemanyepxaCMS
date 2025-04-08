package com.hebe.hebemanyepxa.service

import com.hebe.hebemanyepxa.model.Poetry
import com.hebe.hebemanyepxa.repository.PoetryRepository
import com.hebe.hebemanyepxa.util.SlugUtil
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.Optional

@Service
class PoetryService(private val poetryRepository: PoetryRepository) {

    fun findAll(pageable: Pageable): Page<Poetry> {
        return poetryRepository.findAll(pageable)
    }

    fun findById(id: Long): Optional<Poetry> {
        return poetryRepository.findById(id)
    }

    fun findBySlug(slug: String): Optional<Poetry> {
        return poetryRepository.findBySlug(slug)
    }

    fun findFeatured(): List<Poetry> {
        return poetryRepository.findByIsFeaturedTrue()
    }

    fun findPublished(pageable: Pageable): Page<Poetry> {
        return poetryRepository.findByIsPublishedTrueOrderByPublishDateDesc(pageable)
    }

    fun findByDateRange(startDate: LocalDate, endDate: LocalDate, pageable: Pageable): Page<Poetry> {
        return poetryRepository.findByPublishDateBetweenAndIsPublishedTrue(startDate, endDate, pageable)
    }

    @Transactional
    fun create(poetry: Poetry): Poetry {
        // Generate slug if not provided
        val slug = if (poetry.slug.isBlank()) {
            generateUniqueSlug(poetry.title)
        } else {
            poetry.slug
        }

        // Create new poetry with the generated slug
        val newPoetry = poetry.copy(slug = slug)
        return poetryRepository.save(newPoetry)
    }

    @Transactional
    fun update(id: Long, poetry: Poetry): Optional<Poetry> {
        return poetryRepository.findById(id).map { existingPoetry ->
            val updatedPoetry = poetry.copy(
                id = existingPoetry.id,
                slug = if (poetry.slug != existingPoetry.slug && poetry.slug.isNotBlank()) {
                    generateUniqueSlug(poetry.slug)
                } else {
                    existingPoetry.slug
                },
                createdAt = existingPoetry.createdAt
            )
            poetryRepository.save(updatedPoetry)
        }
    }

    @Transactional
    fun delete(id: Long) {
        poetryRepository.deleteById(id)
    }

    private fun generateUniqueSlug(title: String): String {
        var slug = SlugUtil.toSlug(title)
        var counter = 1

        while (poetryRepository.existsBySlug(slug)) {
            slug = "${SlugUtil.toSlug(title)}-$counter"
            counter++
        }

        return slug
    }

    @Transactional
    fun count(): Long {
        return poetryRepository.count()
    }
}