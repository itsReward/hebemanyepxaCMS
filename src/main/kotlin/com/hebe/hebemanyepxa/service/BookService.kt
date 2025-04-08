package com.hebe.hebemanyepxa.service

import com.hebe.hebemanyepxa.model.Book
import com.hebe.hebemanyepxa.repository.BookRepository
import com.hebe.hebemanyepxa.util.SlugUtil
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.Optional

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val fileStorageService: FileStorageService
) {

    fun findAll(pageable: Pageable): Page<Book> {
        return bookRepository.findAll(pageable)
    }

    fun findById(id: Long): Optional<Book> {
        return bookRepository.findById(id)
    }

    fun findBySlug(slug: String): Optional<Book> {
        return bookRepository.findBySlug(slug)
    }

    fun findFeatured(): List<Book> {
        return bookRepository.findByIsFeaturedTrue()
    }

    fun findByAuthor(author: String, pageable: Pageable): Page<Book> {
        return bookRepository.findByAuthorContainingIgnoreCase(author, pageable)
    }

    @Transactional
    fun create(book: Book, coverImageFile: MultipartFile?): Book {
        // Generate slug if not provided
        val slug = if (book.slug.isBlank()) {
            generateUniqueSlug(book.title)
        } else {
            book.slug
        }

        // Process cover image if provided
        val coverImage = coverImageFile?.let {
            if (!it.isEmpty) {
                fileStorageService.storeFile(it, "books")
            } else null
        }

        // Create new book with the generated slug and cover image path
        val newBook = book.copy(slug = slug, coverImage = coverImage)
        return bookRepository.save(newBook)
    }

    @Transactional
    fun update(id: Long, book: Book, coverImageFile: MultipartFile?): Optional<Book> {
        return bookRepository.findById(id).map { existingBook ->
            val coverImage = if (coverImageFile != null && !coverImageFile.isEmpty) {
                // Delete old cover image if exists
                existingBook.coverImage?.let { fileStorageService.deleteFile(it) }

                // Store new cover image
                fileStorageService.storeFile(coverImageFile, "books")
            } else {
                existingBook.coverImage
            }
            val updatedBook = book.copy(
                id = existingBook.id,
                slug = if (book.slug != existingBook.slug && book.slug.isNotBlank()) {
                    generateUniqueSlug(book.slug)
                } else {
                    existingBook.slug
                },
                coverImage = coverImage,
                createdAt = existingBook.createdAt
            )

            bookRepository.save(updatedBook)
        }
    }

    @Transactional
    fun delete(id: Long) {
        bookRepository.findById(id).ifPresent { book ->
            // Delete cover image if exists
            book.coverImage?.let { fileStorageService.deleteFile(it) }

            // Delete the book entity
            bookRepository.deleteById(id)
        }
    }

    private fun generateUniqueSlug(title: String): String {
        var slug = SlugUtil.toSlug(title)
        var counter = 1

        while (bookRepository.existsBySlug(slug)) {
            slug = "${SlugUtil.toSlug(title)}-$counter"
            counter++
        }

        return slug
    }

    @Transactional
    fun count(): Long {
        return bookRepository.count()
    }
}