package com.hebe.hebemanyepxa.controller

import com.hebe.hebemanyepxa.model.ApparelImage
import com.hebe.hebemanyepxa.service.ApparelService
import com.hebe.hebemanyepxa.service.BookService
import com.hebe.hebemanyepxa.service.PoetryService
import com.hebe.hebemanyepxa.service.QuoteService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus
import java.time.LocalDate

@Controller
class PublicController(
    private val poetryService: PoetryService,
    private val apparelService: ApparelService,
    private val bookService: BookService,
    private val quoteService: QuoteService
) {

    @GetMapping("/")
    fun home(model: Model): String {
        // Featured content for homepage
        model.addAttribute("featuredPoetry", poetryService.findFeatured())
        model.addAttribute("featuredBooks", bookService.findFeatured())
        model.addAttribute("featuredQuotes", quoteService.findFeatured())
        model.addAttribute("featuredApparel", apparelService.findFeatured())

        return "public/index"
    }

    @GetMapping("/about")
    fun about(model: Model): String {
        return "public/about"
    }

    @GetMapping("/contact")
    fun contact(model: Model): String {
        return "public/contact"
    }

    @GetMapping("/books")
    fun books(model: Model): String {
        // Get all books for the books page
        val booksPageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "publishYear"))
        model.addAttribute("books", bookService.findAll(booksPageable))

        return "public/books"
    }

    @GetMapping("/books/{slug}")
    fun bookDetail(@PathVariable slug: String, model: Model): String {
        val book = bookService.findBySlug(slug).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found")
        }
        model.addAttribute("book", book)

        // Related books by same author
        val relatedPageable = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "publishYear"))
        val relatedBooks = bookService.findByAuthor(book.author, relatedPageable).content
            .filter { it.id != book.id }
            .take(3)
        model.addAttribute("relatedBooks", relatedBooks)

        return "public/book-detail"
    }

    @GetMapping("/quotes")
    fun quotes(model: Model): String {
        // Get quotes grouped by category
        val quotesPageable = PageRequest.of(0, 50, Sort.by(Sort.Direction.DESC, "createdAt"))
        val allQuotes = quoteService.findAll(quotesPageable).content

        // Extract unique categories
        val categories = allQuotes
            .mapNotNull { it.category }
            .flatMap { it.split(",").map { cat -> cat.trim() } }
            .distinct()

        model.addAttribute("quotes", allQuotes)
        model.addAttribute("categories", categories)

        return "public/quotes"
    }

    @GetMapping("/blog")
    fun blog(model: Model): String {
        return "public/blog"
    }

    @GetMapping("/poetry")
    fun poetry(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        model: Model
    ): String {
        val poetryPageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishDate"))
        val poetryPage = poetryService.findPublished(poetryPageable)

        model.addAttribute("poetry", poetryPage)
        model.addAttribute("currentPage", page)

        return "public/poetry"
    }

    @GetMapping("/poetry/{slug}")
    fun poetryDetail(@PathVariable slug: String, model: Model): String {
        val poem = poetryService.findBySlug(slug).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Poem not found")
        }
        model.addAttribute("poem", poem)

        // Get previous and next poems
        val allPoems = poetryService.findAll(PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC, "publishDate"))).content
        val currentIndex = allPoems.indexOfFirst { it.id == poem.id }

        if (currentIndex > 0) {
            model.addAttribute("previousPoem", allPoems[currentIndex - 1])
        }
        if (currentIndex < allPoems.size - 1) {
            model.addAttribute("nextPoem", allPoems[currentIndex + 1])
        }

        return "public/poetry-detail"
    }

    @GetMapping("/apparel")
    fun apparel(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "12") size: Int,
        model: Model
    ): String {
        val apparelPageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        val apparelPage = apparelService.findAvailable(apparelPageable)

        model.addAttribute("apparel", apparelPage)
        model.addAttribute("currentPage", page)

        return "public/apparel"
    }

    @GetMapping("/apparel/{slug}")
    fun apparelDetail(@PathVariable slug: String, model: Model): String {
        val apparelItem = apparelService.findBySlug(slug).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Apparel item not found")
        }
        model.addAttribute("apparelItem", apparelItem)

        // Get related items
        val relatedPageable = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "createdAt"))
        val relatedItems = apparelService.findAvailable(relatedPageable).content
            .filter { it.id != apparelItem.id }
            .take(4)
        model.addAttribute("relatedItems", relatedItems)

        model.addAttribute("imageHelper", object {
            fun getPrimaryImagePath(images: List<ApparelImage>): String {
                if (images.isEmpty()) return ""

                val primaryImage = images.find { it.isPrimary }
                return primaryImage?.filePath ?: images[0].filePath
            }
        })

        return "public/apparel-detail"
    }
}